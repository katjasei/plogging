package com.example.plogging.ui.auth

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.plogging.*
import com.example.plogging.ui.home.MainActivity
import com.example.plogging.utils.askPermissions
import com.example.plogging.viewModel.LoginViewModel


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
    // bundle needs for communication between two fragments
    private val bundle = Bundle()
    private val loginViewModel = LoginViewModel()

    //FUNCTIONS:

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        hideSystemUI()
        //permissions
        askPermissions(this,this)
        replaceFragment(splashScreenFragment)
        //check if user already logged in
        observeAuthenticationState()
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

    //function used for fragment replacement
    private fun replaceFragment(fragment: Fragment){
        // fragment manager can help when switching to the other fragment is needed
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
        hideSystemUI()
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
                    //TODO permissions accepted, continue as normal
                }
                else{
                    //permission denied
                    //TODO permissions denied
                }
            }
        }
    }

    private fun observeAuthenticationState() {
        loginViewModel.authenticationState.observe(this, Observer {
            when (it) {
                LoginViewModel.AuthenticationState.AUTHENTICATED -> {
                    val handler = Handler()
                    handler.postDelayed({
                        run {
                            //go to FirstFragment, if user not logged in
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        }
                    },3000)
                }
                else -> {
                    val handler = Handler()
                    handler.postDelayed({
                        run {
                            //go to FirstFragment, if user not logged in
                            replaceFragment(firstFragment)
                        }
                    },3000)}
                }
            })
        }

    private fun hideSystemUI() {
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
        }
    }
