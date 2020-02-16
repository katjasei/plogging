package com.example.plogging

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class UserModel(application: Application): AndroidViewModel(application) {

    private val users: List<User> = UserDB.get(getApplication()).userDao().getAll()
    fun getUsers() = users

}