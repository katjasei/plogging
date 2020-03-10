package com.example.plogging.data.model

//need for recycler view that uses in LeaderBoard
class ClassUserTrash (username:String, trashTotal:Int){

    var username = ""
    var trashTotal = 0

    init{
        this.username = username
        this.trashTotal = trashTotal
    }
}