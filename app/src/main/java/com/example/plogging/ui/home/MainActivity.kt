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
    HomeFragment.HomeFragmentListener, PloggingActivityFragment.PloggingActivityListener,
    PointFragment.PointActivityListener, ProfileFragment.ProfileFragmentListener{

    //firebase auth object
    lateinit var mFirebaseAuth: FirebaseAuth
    //Create a new Fragment to be placed in the activity layout
    private val homeFragment = HomeFragment()
    private val ploggingActivityFragment = PloggingActivityFragment()
    private val afterStopActivityFragment = AfterStopActivityFragment()
    private val pointFragment = PointFragment()
    private val profileFragment = ProfileFragment()

    // bundle needs for communication between two fragments
    private val bundle = Bundle()

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
                replaceFragment(LeaderBoardFragment())
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

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        //hideSystemUI()

        //Set listener to bottom navigation
        bottom_navigation.setOnNavigationItemSelectedListener(bottomNavigationOnClickListener)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container, homeFragment)
            .commit()
    }

    override fun onStart() {
        super.onStart()
        //hide status bar
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }

    //when user click button LOGOUT
    override fun onButtonLogOutClick() {
        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseAuth.signOut()
        //start new Activity - go to FirstScreen/LogIn, SighUp screen
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
    }
    //HomeFragment listener
    //when button "Start activity" clicked from HomeFragment
    override fun onButtonStartActivityClick() {
        hideBottomNavigation()
        ploggingActivityFragment.resetStepCounter()
        replaceFragment(ploggingActivityFragment)
    }

    //PloggingActivityFragment listener
    //when button "Stop activity" clicked from PloggingActivityFragment
    override fun onButtonStopActivityClick() {
        replaceFragment(afterStopActivityFragment)
    }

    //PointActivityFragment listener
    //when button "GoToProfile" clicked from PointFragment
    override fun onButtonGoToProfileClick() {
        replaceFragment(profileFragment)
    }

    //AfterStopActivityFragment listener
    //when button "Upload" clicked from AfterStopActivityFragment
    override fun onButtonUploadClick(points:String) {
        replaceFragment(pointFragment)
        bundle.putCharSequence("points" , points)
        pointFragment.arguments = bundle
        showBottomNavigation()
    }

    private fun replaceFragment(fragment: Fragment) {

        //if fragment is homeFragment, display bottom navigation
        if (fragment == homeFragment) {
            showBottomNavigation()
        }

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


    private fun showBottomNavigation() {
        bottom_navigation.visibility = View.VISIBLE
    }

    private fun hideBottomNavigation() {
        bottom_navigation.visibility = View.GONE
    }

    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

}