package com.example.plogging.ui.home

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.plogging.R
import com.example.plogging.ui.auth.AuthActivity
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), AfterStopActivityFragment.AfterStopActivityListener,
    HomeFragment.HomeFragmentListener, PointFragment.PointActivityListener,
    ProfileFragment.ProfileFragmentListener, NoInternetFragment.NoInternetFragmentListener {

    //firebase auth object
    private var mFirebaseAuth = FirebaseAuth.getInstance()
    //Create a new Fragment to be placed in the activity layout
    private val noInternetFragment = NoInternetFragment()
    private val homeFragment = HomeFragment()
    private val ploggingActivityFragment = PloggingActivityFragment()
    private val afterStopActivityFragment = AfterStopActivityFragment()
    private val pointFragment = PointFragment()
    private val profileFragment = ProfileFragment()


    // bundle needs for communication between two fragments
    private val bundle = Bundle()

    //Bottom navigation click listener
    private val bottomNavigationOnClickListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.weather -> {
                Log.i("TAG", "${item.title} pressed")
                if (isNetworkAvailable(this)) {
                    replaceFragment(WeatherFragment())
                } else {
                    replaceFragment(noInternetFragment)
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.leaderboard -> {
                Log.i("TAG", "${item.title} pressed")
                if (isNetworkAvailable(this)){
                    replaceFragment(LeaderBoardFragment())
                } else {
                    replaceFragment(noInternetFragment)
                }

                return@OnNavigationItemSelectedListener true
            }
            R.id.home -> {
                Log.i("TAG", "${item.title} pressed")
                replaceFragment(HomeFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.profile -> {
                Log.i("TAG", "${item.title} pressed")

                if(mFirebaseAuth.currentUser != null){
                    replaceFragment(ProfileFragment())
                }
                else {
                    val intent = Intent(this, NotRegisteredActivity::class.java)
                    startActivity(intent)
                }
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //hide status bar
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        //Set listener to bottom navigation
        bottom_navigation.setOnNavigationItemSelectedListener(bottomNavigationOnClickListener)
        replaceFragment(homeFragment)
    }


    //when user click button LOGOUT
    override fun onButtonLogOutClick() {
        //user logout
        mFirebaseAuth.signOut()
        //start new Activity - go to FirstScreen/LogIn, SighUp screen
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        hideSystemUI()
    }

    //HomeFragment listener
    //when button "Start activity" clicked from HomeFragment
    override fun onButtonStartActivityClick() {
        hideBottomNavigation()
        homeFragment.resetStepCounter()
       // replaceFragment(ploggingActivityFragment)
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

    override fun onButtonPloggingResultClick() {
        replaceFragment(afterStopActivityFragment)
    }
    //HomeFragment listener
    //when button "Plogging Result" clicked from HomeFragment
    private fun replaceFragment(fragment: Fragment) {
        //if fragment is homeFragment, display bottom navigation
        if (fragment == homeFragment) {
            showBottomNavigation()
        }
        supportFragmentManager.beginTransaction()
        .replace(R.id.fragment_container, fragment)
        .addToBackStack(null)
        .commit()
        hideSystemUI()
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                }
            }
        return false
    }


    private fun showBottomNavigation() {
        bottom_navigation.visibility = View.VISIBLE
    }

    private fun hideBottomNavigation() {
        bottom_navigation.visibility = View.GONE
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

    override fun reloadWeather() {
        if (isNetworkAvailable(this)) {
            replaceFragment(WeatherFragment())
        } else {
            replaceFragment(NoInternetFragment())
        }
    }

    override fun getRoute(): MutableList<LatLng> {
        return homeFragment.routePoints
    }

    override fun getRouteLength(): Double {
        return homeFragment.routeLength
    }

    override fun getRouteTime(): Int {
        return homeFragment.seconds
    }
}