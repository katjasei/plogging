package com.example.plogging.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.plogging.R
import com.example.plogging.ui.auth.AuthActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), HomeFragment.HomeFragmentListener {

    //firebase auth object
    lateinit var mFirebaseAuth: FirebaseAuth
    //Create a new Fragment to be placed in the activity layout
    private val homeFragment = HomeFragment()
    private val ploggingActivityFragment = PloggingActivityFragment()

    //Bottom navigation click listener
    private val bottomNavigationOnClickListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.weather -> {
                Log.i("TAG", "${item.title} pressed")
                replaceFragment(WeatherFragment())
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

    //when user click button StartActivity
    override fun onButtonStartActivityClick() {
        supportFragmentManager.beginTransaction().replace(
            R.id.fragment_container,
            ploggingActivityFragment)
            .addToBackStack(null )
            .commit()

    }

    private fun replaceFragment(fragment: Fragment){
        Log.i("TAG", fragment.toString())
        val manager = supportFragmentManager.beginTransaction()
        manager.replace(R.id.fragment_container, fragment)
        manager.commit()
    }
}