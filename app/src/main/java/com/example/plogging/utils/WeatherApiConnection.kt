package com.example.plogging.utils

import android.os.Handler
import android.util.Log
import java.io.InputStream
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL

//Makes api request and sends string result to handler to be used

class WeatherApiConnection(mHand: Handler): Runnable {
    private val myHandler = mHand
    //this api is not really reliable, 50% of time returns wrong city info but does not require api key
    private val url = URL(" https://fcc-weather-api.glitch.me/api/current?lat=60.1&lon=24.9")

    override fun run() {
        try {
            val connection = url.openConnection() as HttpURLConnection
            val inputStream: InputStream = connection.inputStream
            val inputText = inputStream.bufferedReader().use {
                it.readText()
            }
            val result = StringBuilder()
                result.append(inputText)

            Log.i("TAG", result.toString())

            //construct message sent to handler
            val message = myHandler.obtainMessage()
            message.what = 0
            message.obj = result.toString()
            myHandler.sendMessage(message)
        }
        catch(e: Exception) {
            Log.i("TAG", "Caught exception trying to connect to weather api. Message: ${e.message}")
        }
    }
}