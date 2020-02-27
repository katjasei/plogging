package com.example.plogging.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.example.plogging.*
import com.example.plogging.ui.home.HomeActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(), FirstFragment.FirstFragmentListener,
    RegistrationFragment.RegistrationFragmentListener, WelcomeFragment.WelcomeFragmentListener
{
    //Create a new Fragment to be placed in the activity layout
    private val firstFragment = FirstFragment()
    private val loginFragment = LoginFragment()
    private val registrationFragment = RegistrationFragment()
    private val welcomeFragment = WelcomeFragment()
    private val splashScreenFragment = SplashScreenFragment()

    //firebase auth object
    lateinit var mFirebaseAuth: FirebaseAuth

    // bundle needs for communication between two fragments
    private val bundle = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Hide the status bar.
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN


        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container, splashScreenFragment)
            .commit()

        mFirebaseAuth = FirebaseAuth.getInstance()

        //if the objects getcurrentuser is not null
        //means u
        // ser is already logged in
        if(mFirebaseAuth.currentUser != null){
            //if user is logged in go to HomeActvity - "Home or Map Screen"
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        } else {
            val handler = Handler()
            handler.postDelayed({
                run {
                    //go to FirstFragment, if user not logged in
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fragment_container,
                        firstFragment)
                        .addToBackStack(null )
                        .commit()
                }
            },4000)}
    }
    //FirstFragment listeners:
    //when button "sign up" clicked from FirstFragment
    override fun onButtonSignUpClick() {
        supportFragmentManager.beginTransaction().replace(
            R.id.fragment_container,
            registrationFragment)
            .addToBackStack(null )
            .commit()
    }
    //when button "sign in" clicked from FirstFragment
    override fun onButtonSignInClick() {
        supportFragmentManager.beginTransaction().replace(
            R.id.fragment_container,
            loginFragment)
            .addToBackStack(null )
            .commit()
    }

    //RegistrationFragment listener
    //when button "sign up" clicked from RegistrationFragment
    override fun onButtonSignUpClickFromRegistration(username: String) {
        supportFragmentManager.beginTransaction().replace(
            R.id.fragment_container,
            welcomeFragment)
            .addToBackStack(null )
            .commit()
        bundle.putCharSequence("username" , username)
        welcomeFragment.arguments = bundle

    }

    //WelcomeFragment listener
    //when button "Start plogging" clicked from WelcomeFragment
    override fun onButtonStartPloggingClick() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }
}
