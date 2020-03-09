package com.example.plogging.data.model

import com.google.android.gms.maps.model.LatLng

class ClassRoute (distance: Double, route: MutableList<LatLng>, time: Int){

    var distance = 0.0
    var points = LatLng(0.0,0.0)
    var time = 0

    init {
        this.distance = distance
        this.points = points
        this.time = time
    }
}