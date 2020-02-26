package com.example.plogging.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.plogging.R
import com.example.plogging.ui.auth.MainActivity
import com.google.firebase.auth.FirebaseAuth


class HomeActivity : AppCompatActivity(), HomeFragment.HomeFragmentListener {

    //firebase auth object
    lateinit var mFirebaseAuth: FirebaseAuth
    //Create a new Fragment to be placed in the activity layout
    private val homeFragment = HomeFragment()
    private val ploggingActivityFragment = PloggingActivityFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container, homeFragment)
            .commit()

    }
    //when user click button LOGOUT
    override fun onButtonLogOutClick() {
        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseAuth.signOut()
        val intent = Intent(this, MainActivity::class.java)
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


    fun setActionBarTitle(title:String){
        supportActionBar!!.title = title
    }

}