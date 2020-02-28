package com.example.plogging.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.plogging.R
import com.example.plogging.utils.WeatherApiConnection
import com.example.plogging.utils.WeatherApiResponseParser
import kotlinx.android.synthetic.main.fragment_weather.*

class WeatherFragment: Fragment(){

    private val handler: Handler = object :
    Handler(Looper.getMainLooper()) {
        override fun handleMessage(inputMessage: Message) {
            if (inputMessage.what == 0) {
                //parse response
                val weatherObject = WeatherApiResponseParser.parse(inputMessage.obj.toString())
                //set to a textview
                weatherText.text = weatherObject.main.temp.toString()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val weatherConnection =
            WeatherApiConnection(handler)
        val weatherThread = Thread(weatherConnection)
        weatherThread.start()

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

}