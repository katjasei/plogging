package com.example.plogging

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity(), FirstFragment.FirstFragmentListener, RegistrationFragment.RegistrationFragmentListener
{


    //Create a new Fragment to be placed in the activity layout
    private val firstFragment = FirstFragment()
    private val loginFragment = LoginFragment()
    private val registrationFragment = RegistrationFragment()
    private val welcomeFragment = WelcomeFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager
            .beginTransaction()
            .add(R.id. fragment_container, firstFragment)
            .commit()
    }
    //FirstFragment listeners:
    //when button "sign up" clicked from FirstFragment
    override fun onButtonSignUpClick() {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
            registrationFragment)
            .addToBackStack(null )
            .commit()
    }
    //when button "sign in" clicked from FirstFragment
    override fun onButtonSignInClick() {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
            loginFragment)
            .addToBackStack(null )
            .commit()
    }

    //RegistrationFragment listener
    //when button "sign up" clicked from RegistrationFragment
    override fun onButtonSignUpClickFromRegistration() {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
            welcomeFragment)
            .addToBackStack(null )
            .commit()
    }
}
