package com.example.plogging.data.model

import android.media.MicrophoneInfo
import com.google.gson.annotations.SerializedName

class Weather {

    data class Sys (
        val type : Int,
        val id : Int,
        val message : Double,
        val country : String,
        val sunrise : Int,
        val sunset : Int
    )

    data class Clouds (
        val all : Int
    )

    data class Wind (
        val speed : Double,
        val deg : Int
    )

    data class Main (
        val temp : Double,
        val pressure : Int,
        val humidity : Int,
        val temp_min : Int,
        val temp_max : Int
    )

    data class Coord(
        val lon : Int,
        val lat : Int
    )

    data class Base(
        val coord : Coord,
        val weather : List<Weather>,
        val base : String,
        val main : Main,
        val visibility : Int,
        val wind : Wind,
        val clouds : Clouds,
        val dt : Int,
        val sys : Sys,
        val id : Int,
        val name : String,
        val cod : Int
    )
}