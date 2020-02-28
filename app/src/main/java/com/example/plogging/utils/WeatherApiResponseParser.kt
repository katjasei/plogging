package com.example.plogging.utils

import com.example.plogging.data.model.Weather
import com.google.gson.Gson

object WeatherApiResponseParser {

    fun parse(apiResponse: String): Weather.Base {
        return Gson().fromJson(apiResponse, Weather.Base::class.java)
    }
}