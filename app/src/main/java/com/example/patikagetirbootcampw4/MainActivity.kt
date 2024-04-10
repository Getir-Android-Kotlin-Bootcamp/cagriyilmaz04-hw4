package com.example.patikagetirbootcampw4

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.os.StrictMode
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.example.patikagetirbootcampw4.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch(Dispatchers.IO) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)

            val login = Login("fatih1@gmail.com", "Gptmap123")
            val loginResponse = makePostRequest(login)


            if (loginResponse.startsWith("Hata:")) {
                launch(Dispatchers.Main) {

                    binding.textViewPost.text = loginResponse
                    binding.textViewGet.text = loginResponse.subSequence(0,loginResponse.length-16)
                }
            } else {
                val profileResponse = makeGetRequest(loginResponse)

                launch(Dispatchers.Main) {
                    binding.textViewPost.text = loginResponse
                    binding.textViewGet.text = profileResponse
                }
            }
        }
    }

    private fun makePostRequest(loginData: Login): String {
        val urlString = "https://espresso-food-delivery-backend-cc3e106e2d34.herokuapp.com/login"
        val jsonData = loginToJson(loginData)

        try {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.doOutput = true
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8")

            OutputStreamWriter(connection.outputStream).use { writer ->
                writer.write(jsonData)
            }

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader(InputStreamReader(connection.inputStream)).use { reader ->
                    return reader.readText()
                }
            } else {
                return "Hata: Sunucudan beklenmeyen bir yanıt alındı. Yanıt kodu: $responseCode"
            }
        } catch (e: Exception) {
            return "Hata: İstek sırasında bir hata oluştu. Hata mesajı: ${e.message}"
        }
    }

    private fun loginToJson(login: Login): String {
        return "{\"email\":\"${login.email}\",\"password\":\"${login.password}\"}"
    }


    private fun makeGetRequest(userId: String): String {
        val urlString = "https://espresso-food-delivery-backend-cc3e106e2d34.herokuapp.com/profile/$userId"
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        return BufferedReader(InputStreamReader(connection.inputStream)).use { reader ->

            reader.readText().toString()
        }
    }

}
