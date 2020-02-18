package com.example.plogging

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    val uid: Long,
    val username: String,
    val email: String,
    val password: String) {
    //constructor, getter and setter are implicit :)
    override fun toString() = "($uid) $username $email $password"
}