package com.example.plogging.ui.home


import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import com.example.plogging.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_plogging_activity.*


class PloggingActivityFragment: Fragment(), OnMapReadyCallback, SensorEventListener {


    private lateinit var sensorManager: SensorManager
    private var stepCounterSensor: Sensor? = null
    private var activityCallBack: PloggingActivityListener? = null
    private lateinit var  fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var currentLocation: LatLng

    interface PloggingActivityListener {
        fun onButtonStopActivityClick()
    }

    override fun onAttach(context: Context)   {
        super.onAttach(context)
        activityCallBack =  context as PloggingActivityListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        sensorManager = activity?.getSystemService(Context.SENSOR_SERVICE) as SensorManager

        checkForStepCounterSensor()

        return inflater.inflate(R.layout.fragment_plogging_activity, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btn_stop_activity.setOnClickListener {
            activityCallBack!!.onButtonStopActivityClick()
        }

        floating_action_button.setOnClickListener {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context!!.applicationContext)
            val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)
        }
    }

    override fun onStart() {
        super.onStart()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context!!.applicationContext)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location ->
             currentLocation = LatLng(location.latitude, location.longitude)
            map.addMarker(
                MarkerOptions()
                    .position(currentLocation)
                    .title("Your current location")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            )
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f))
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.i("sensor", "Accuracy changed")
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor == stepCounterSensor) {
            Log.i("sensor", "Sensor data: ${event.values[0]}")
            stepTextView.text = event.values[0].toString()
        }
    }

    override fun onResume() {
        super.onResume()
        stepCounterSensor?.also {
            sensorManager.registerListener(this, it,
                SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    private fun checkForStepCounterSensor() {
        if (sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) != null) {
            stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
            Log.i("TAG", "Sensor found")
        } else {
            Log.i("TAG", "No sensor available")
            //TODO disable sensor activity
        }
    }

}