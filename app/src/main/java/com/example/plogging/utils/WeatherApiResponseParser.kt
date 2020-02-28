package com.example.plogging.utils

import com.example.plogging.data.model.Weather
import com.google.gson.Gson

object WeatherApiResponseParser {

    fun parse(apiResponse: String): Weather {
        return Gson().fromJson(apiResponse, Weather::class.java)
    }
}