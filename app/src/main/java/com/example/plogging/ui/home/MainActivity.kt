package com.example.plogging.ui.home

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.plogging.R
import com.example.plogging.ui.auth.AuthActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), AfterStopActivityFragment.AfterStopActivityListener,
    HomeFragment.HomeFragmentListener, PloggingActivityFragment.PloggingActivityListener {

    //firebase auth object
    lateinit var mFirebaseAuth: FirebaseAuth
    //Create a new Fragment to be placed in the activity layout
    private val homeFragment = HomeFragment()
    private val ploggingActivityFragment = PloggingActivityFragment()
    private val afterStopActivityFragment = AfterStopActivityFragment()
    private val pointFragment = PointFragment()

    //Bottom navigation click listener
    //TODO maybe take this logic to a new file
    private val bottomNavigationOnClickListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.weather -> {
                Log.i("TAG", "${item.title} pressed")
                if (isNetworkAvailable()) {
                replaceFragment(WeatherFragment()) }
                else {
                    //TODO No network connection screen
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.leaderboard -> {
                Log.i("TAG", "${item.title} pressed")
                replaceFragment(LeaderboardFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.home -> {
                Log.i("TAG", "${item.title} pressed")
                replaceFragment(HomeFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.profile -> {
                Log.i("TAG", "${item.title} pressed")
                replaceFragment(ProfileFragment())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Hide the status bar.
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        //Set listener to bottom navigation
        bottom_navigation.setOnNavigationItemSelectedListener(bottomNavigationOnClickListener)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container, homeFragment)
            .commit()
    }
    
    //when user click button LOGOUT
    override fun onButtonLogOutClick() {
        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseAuth.signOut()
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
    }
    //HomeFragment listener
    //when button "Start activity" clicked from HomeFragment
    override fun onButtonStartActivityClick() {
        replaceFragment(ploggingActivityFragment)
    }

    //PloggingActivityFragment listener
    //when button "Stop activity" clicked from PloggingActivityFragment
    override fun onButtonStopActivityClick() {
        replaceFragment(afterStopActivityFragment)
    }

    //AfterStopActivityFragment listener
    //when button "Upload" clicked from AfterStopActivityFragment
    override fun onButtonUploadClick() {
        replaceFragment(pointFragment)
    }

    private fun replaceFragment(fragment: Fragment){
        Log.i("TAG", fragment.toString())
        supportFragmentManager.beginTransaction()
        .replace(R.id.fragment_container, fragment)
        .addToBackStack(null)
        .commit()
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = this.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        //if activeNetworkInfo == false -> if isConnected == false -> return false
        return connectivityManager.activeNetworkInfo?.isConnected?:false
    }

    override fun onResume() {
        super.onResume()
        // Hide the status bar.
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }
}