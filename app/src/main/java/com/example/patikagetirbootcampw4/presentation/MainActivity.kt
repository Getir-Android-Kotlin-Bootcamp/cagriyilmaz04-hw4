package com.example.patikagetirbootcampw4.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.os.StrictMode
import androidx.lifecycle.lifecycleScope
import com.example.patikagetirbootcampw4.R
import com.example.patikagetirbootcampw4.databinding.ActivityMainBinding
import com.example.patikagetirbootcampw4.model.Login
import com.example.patikagetirbootcampw4.model.User
import com.example.patikagetirbootcampw4.network.NetworkManager
import com.example.patikagetirbootcampw4.util.UserUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val networkManager = NetworkManager.getInstance()
        val userUtils = UserUtils()

        lifecycleScope.launch(Dispatchers.IO) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)

            val login = Login("fatih1@gmail.com", "Gptmap123")
            val loginResponse = networkManager.makePostRequest(login)

            if (loginResponse.startsWith("Error:")) {
                launch(Dispatchers.Main) {
                    val errorMessage = getString(R.string.error_message)
                    binding.textViewPost.text = "${errorMessage} $loginResponse"
                    binding.textViewGet.text = errorMessage.subSequence(0,errorMessage.length-12)
                }
            } else {
                val profileResponse = networkManager.makeGetRequest(loginResponse)
                val user = userUtils.parseUserJson(profileResponse)
                displayUserInfo(user)
                launch(Dispatchers.Main) {
                    binding.textViewPost.text = loginResponse
                }
            }
        }
    }

    private fun displayUserInfo(user: User) {
        val userInfo = "ID: ${user.id}\n" +
                "Full Name: ${user.fullName}\n" +
                "Email: ${user.email}\n" +
                "Phone: ${user.phoneNumber ?: "Not provided"}\n" +
                "Occupation: ${user.occupation ?: "Not provided"}\n" +
                "Employer: ${user.employer ?: "Not provided"}\n"+
                "Country: ${user.country ?: "Not provided"}\n" +
                "Latitude: ${user.latitude}\n" +
                "Longitude: ${user.longitude}\n"

        binding.textViewGet.text = userInfo
    }
}
