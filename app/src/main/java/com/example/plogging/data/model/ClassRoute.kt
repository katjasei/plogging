package com.example.plogging.data.model

import com.google.android.gms.maps.model.LatLng

class ClassRoute (distance: Double, route: MutableList<LatLng>){

    var distance = 0.0
    var points = LatLng(0.0,0.0)

    init {
        this.distance = distance
        this.points = points
    }
}