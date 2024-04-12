package com.example.patikagetirbootcampw4.network
import com.example.patikagetirbootcampw4.model.Login
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
class NetworkManager {
    fun makePostRequest(loginData: Login): String {
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
                return "Error: $responseCode"
            }
        } catch (e: Exception) {
            return "Error: ${e.message}"
        }
    }

    private fun loginToJson(login: Login): String {
        return "{\"email\":\"${login.email}\",\"password\":\"${login.password}\"}"
    }

    fun makeGetRequest(userId: String): String {
        val urlString = "https://espresso-food-delivery-backend-cc3e106e2d34.herokuapp.com/profile/$userId"
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        return BufferedReader(InputStreamReader(connection.inputStream)).use { reader ->
            reader.readText()
        }
    }

    companion object {
        fun getInstance(): NetworkManager = NetworkManager()
    }
}
