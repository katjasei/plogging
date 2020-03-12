package com.example.plogging.viewModel

import androidx.lifecycle.ViewModel
import com.example.plogging.data.liveData.FirebaseUserLiveData
import androidx.lifecycle.map

class LoginViewModel : ViewModel() {

    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED
    }

    val authenticationState = FirebaseUserLiveData().map { user ->
        if (user != null) {
            AuthenticationState.AUTHENTICATED
        } else {
            AuthenticationState.UNAUTHENTICATED
        }
    }

}