package com.example.plogging.ui.auth

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.plogging.*
import com.example.plogging.ui.home.MainActivity
import com.google.firebase.auth.FirebaseAuth

class AuthActivity : AppCompatActivity(), FirstFragment.FirstFragmentListener,
    RegistrationFragment.RegistrationFragmentListener, WelcomeFragment.WelcomeFragmentListener
{
    // Permission code
    private val PERMISSIONS = 1

    //Create a new Fragment to be placed in the activity layout
    private val firstFragment = FirstFragment()
    private val loginFragment = LoginFragment()
    private val registrationFragment = RegistrationFragment()
    private val welcomeFragment = WelcomeFragment()
    private val splashScreenFragment = SplashScreenFragment()

    //firebase auth object
    private lateinit var mFirebaseAuth: FirebaseAuth

    // bundle needs for communication between two fragments
    private val bundle = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        // Hide the status bar
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        askPermissions()

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
            val intent = Intent(this, MainActivity::class.java)
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
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    //Location, (TODO) step sensor
    private fun askPermissions() {
        val permissionsRequired = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.CAMERA)

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            &&
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            //permission was already granted
        }
        else{
            //permission not granted, request
            ActivityCompat.requestPermissions(this, permissionsRequired,
                PERMISSIONS)
        }
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
    override fun onResume() {
        super.onResume()
        // Hide the status bar.
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }
}
