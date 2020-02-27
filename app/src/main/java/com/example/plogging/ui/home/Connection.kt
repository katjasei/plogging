package com.example.plogging.ui.home

import android.os.Handler
import android.util.Log
import java.io.InputStream
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL

class Connection(mHand: Handler): Runnable {
    private val myHandler = mHand
    private val url = URL("https://api.weather.gov/points/39.7456,-97.0892")

    override fun run() {
        try {
            val connection = url.openConnection() as HttpURLConnection
            val inputStream: InputStream = connection.inputStream
            val inputText = inputStream.bufferedReader().use {
                it.readText()
            }
            val result = StringBuilder()
                result.append(inputText)

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