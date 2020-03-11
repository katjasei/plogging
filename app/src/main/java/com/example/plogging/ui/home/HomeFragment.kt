package com.example.plogging.ui.home

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.plogging.R
import com.example.plogging.viewModel.LoginViewModel
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import kotlinx.android.synthetic.main.fragment_home.*
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import androidx.lifecycle.Observer


class HomeFragment: Fragment(), OnMapReadyCallback  {

    private var activityCallBack: HomeFragmentListener? = null
    private lateinit var  fusedLocationProviderClient: FusedLocationProviderClient
    private var running= false
    private var seconds = 0
    private val loginViewModel = LoginViewModel()

    interface HomeFragmentListener {
        fun onButtonStartActivityClick()
        fun onButtonPloggingResultClick()
    }
    override fun onAttach(context: Context)   {
        super.onAttach(context)
        activityCallBack =  context as HomeFragmentListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val startButton = view.findViewById<Button>(R.id.btn_start_activity)
        val stopButton = view.findViewById<Button>(R.id.btn_stop_activity_home)
        val resultButton = view.findViewById<Button>(R.id.btn_plogging_result_home)
        val duration = view.findViewById<TextView>(R.id.value_duration)
        seconds = 0
        //if user click button "StartActivity"
        startButton.setOnClickListener {
            // activityCallBack!!.onButtonStartActivityClick()
            startButton.visibility = View.INVISIBLE
            stopButton.visibility = View.VISIBLE
            running = true
            runTimer(duration)
        }

        stopButton.setOnClickListener {
            running = false
            resultButton.visibility = View.VISIBLE
        }

        resultButton.setOnClickListener {
            observeAuthenticationState()
        }

        return view
    }

    private fun observeAuthenticationState() {
        loginViewModel.authenticationState.observe(this, Observer {
            when (it) {
                LoginViewModel.AuthenticationState.AUTHENTICATED -> {
                    activityCallBack!!.onButtonPloggingResultClick()
                }
                else -> {
                    val intent = Intent(context, NotRegisteredActivity::class.java)
                    startActivity(intent)
                }
            }

        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //
        floating_action_button.setOnClickListener {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context!!.applicationContext)
            val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)
        }

        //FAB - set white tint for icon
        val myFabSrc = resources.getDrawable(R.drawable.ic_my_location_white_24dp,null)
        val willBeWhite = myFabSrc?.constantState?.newDrawable()
        willBeWhite?.mutate()?.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY)
        floating_action_button.setImageDrawable(willBeWhite)
    }

    override fun onStart() {
        super.onStart()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context!!.applicationContext)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location ->
            val currentLocation = LatLng(location.latitude, location.longitude)
            map.addMarker(
                MarkerOptions()
                    .position(currentLocation)
                    .title("Your current location")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            )
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f))
        }
    }

    //TODO: move this fun to separate file
    private fun runTimer(textView: TextView){
        val handler = Handler()

        handler.post ( object : Runnable {
            override fun run() {
                val hours = seconds/3600
                val minutes = (seconds%3600)/60
                val secs = seconds%60
                val time = String.format("%d:%02d:%02d", hours,minutes,secs)
                textView.text = time
                if(running) {
                    seconds+=1
                    Log.d("seconds", seconds.toString())
                }
                handler.postDelayed(this,1000)
            }
        })
    }
}
