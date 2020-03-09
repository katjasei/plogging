package com.example.plogging.ui.auth

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.plogging.*
import com.example.plogging.ui.home.MainActivity
import com.example.plogging.utils.askPermissions
import com.google.firebase.auth.FirebaseAuth

class AuthActivity : AppCompatActivity(), FirstFragment.FirstFragmentListener,
    RegistrationFragment.RegistrationFragmentListener, WelcomeFragment.WelcomeFragmentListener
{
    //VARIABLES:
    // Permission code
    private val PERMISSIONS = 1
    //Create a new Fragment to be placed in the activity layout
    private val firstFragment = FirstFragment()
    private val loginFragment = LoginFragment()
    private val registrationFragment = RegistrationFragment()
    private val welcomeFragment = WelcomeFragment()
    private val splashScreenFragment = SplashScreenFragment()
    //firebase auth object
    private var mFirebaseAuth = FirebaseAuth.getInstance()
    // bundle needs for communication between two fragments
    private val bundle = Bundle()

    //FUNCTIONS:
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        // Hide the status bar
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        //permissions
        askPermissions(this,this)
        // fragment manager can help when switching to the other fragment is needed
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container, splashScreenFragment)
            .commit()
        //if the objects getCurrentUser is not null
        //means user is already logged in
        if(mFirebaseAuth.currentUser != null){
            //if user is logged in go to HomeActvity - "Home or Map Screen"
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        else {
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

            },3000)}
    }

    //FirstFragment listeners:
    //when button "sign up" clicked from FirstFragment
    override fun onButtonSignUpClick() {
        replaceFragment(registrationFragment)
    }
    //when button "sign in" clicked from FirstFragment
    override fun onButtonSignInClick() {
       replaceFragment(loginFragment)
    }

    //RegistrationFragment listener
    //when button "sign up" clicked from RegistrationFragment
    override fun onButtonSignUpClickFromRegistration(username: String) {
        replaceFragment(welcomeFragment)
        bundle.putCharSequence("username" , username)
        welcomeFragment.arguments = bundle
    }

    //WelcomeFragment listener
    //when button "Start plogging" clicked from WelcomeFragment
    override fun onButtonStartPloggingClick() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onButtonGoPlogginClick() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun replaceFragment(fragment: Fragment){
        Log.i("TAG", fragment.toString())
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSIONS -> {
                //if request is cancelled, the result array is empty
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission granted
                }
                else{
                    //permission denied
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        //hide status bar
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }

}
