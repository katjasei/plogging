package com.example.plogging.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.plogging.R
import kotlinx.android.synthetic.main.fragment_weather.*

class WeatherFragment: Fragment(){

    private val handler: Handler = object :
    Handler(Looper.getMainLooper()) {
        override fun handleMessage(inputMessage: Message) {
            if (inputMessage.what == 0) {
                weatherText.text = inputMessage.obj.toString()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val weatherConnection = Connection(handler)
        val weatherThread = Thread(weatherConnection)
        weatherThread.start()

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

}