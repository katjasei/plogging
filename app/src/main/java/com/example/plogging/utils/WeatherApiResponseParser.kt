package com.example.plogging.utils

import com.example.plogging.data.model.WeatherModel
import com.google.gson.Gson

object WeatherApiResponseParser {

    fun parse(apiResponse: String): WeatherModel.Base {
        return Gson().fromJson(apiResponse, WeatherModel.Base::class.java)
    }
}