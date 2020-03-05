package com.example.plogging.ui.home

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.graphics.Color
import android.graphics.PorterDuff
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.plogging.R
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.fragment_plogging_activity.*
import java.lang.Error
import kotlinx.android.synthetic.main.fragment_plogging_activity.floating_action_button

class PloggingActivityFragment: Fragment(), OnMapReadyCallback, SensorEventListener {

    private lateinit var startPoint: LatLng
    private var routePoints = mutableListOf<LatLng>()
    private lateinit var marker: MarkerOptions
    private lateinit var startMarker: MarkerOptions
    private var stepsBeforeStart: Float = 1f
    private var firstStep = true
    private lateinit var locationMap: GoogleMap
    private lateinit var lastLocation: Location
    private lateinit var lastLocationLatLng: LatLng
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private var locationUpdateState = false
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setup location callback (when location changes, do this)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                lastLocation = p0.lastLocation
                lastLocationLatLng = LatLng(lastLocation.latitude, lastLocation.longitude)
                Log.i("location", "Location update: $lastLocationLatLng")

                //markers cannot be removed, clear and draw everything again
                locationMap.clear()

                marker = MarkerOptions()
                    .position(lastLocationLatLng)
                    .title("Your current location")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.dot))

                startMarker = MarkerOptions()
                    .position(startPoint)
                    .title("Start point")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))

                //add startMarker and current location marker
                locationMap.addMarker(marker)
                locationMap.addMarker(startMarker)

                //move camera according to location update
                locationMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 20f))

                //add point to list
                routePoints.add(lastLocationLatLng)

                //add polyline between locations
                locationMap.addPolyline(
                    PolylineOptions()
                        .addAll(routePoints)
                        .width(20f).color(Color.parseColor("#801B60FE")).geodesic(true)
                )
            }
        }
        createLocationRequest()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        sensorManager = activity?.getSystemService(Context.SENSOR_SERVICE) as SensorManager

        checkForStepCounterSensor()

        return inflater.inflate(R.layout.fragment_plogging_activity, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btn_stop_activity.setOnClickListener {
            activityCallBack!!.onButtonStopActivityClick()
        }
        //FAB - set white tint for icon
        val myFabSrc = resources.getDrawable(R.drawable.ic_my_location_white_24dp,null)
        val willBeWhite = myFabSrc?.constantState?.newDrawable()
        willBeWhite?.mutate()?.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY)
        floating_action_button.setImageDrawable(willBeWhite)

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

        //start location updates
        locationUpdateState = true
        startLocationUpdates()
    }

    override fun onMapReady(map: GoogleMap) {
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location ->
             currentLocation = LatLng(location.latitude, location.longitude)
            routePoints.add(currentLocation)
            startPoint = currentLocation
            map.addMarker(
                MarkerOptions()
                    .position(currentLocation)
                    .title("Start location")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            )

            map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f))
            locationMap = map

            //sample polyline
/*
            locationMap.addPolyline(
                PolylineOptions()
                    .add(LatLng(60.208957, 24.973649))
                    .add(LatLng(60.208972, 24.974514))
                    .width(20f).color(Color.parseColor("#801B60FE")).geodesic(true)
            )

 */

        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.i("sensor", "Accuracy changed")
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor == stepCounterSensor) {
            if (!firstStep) {
            Log.i("sensor", "Sensor data: ${event.values[0]}")
            stepTextView.text = (event.values[0] - stepsBeforeStart).toString()
            } else {
                stepTextView.text = "0"
                //first event, check the sensor value and set it to stepsBeforeStart to calculate steps during this plogging
                stepsBeforeStart = event.values[0]
                firstStep = false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        //register sensor listener
        stepCounterSensor?.also {
            sensorManager.registerListener(this, it,
                SensorManager.SENSOR_DELAY_NORMAL)
        }
        //start location updates if not already on
        if (!locationUpdateState) {
            startLocationUpdates()
        }
    }

    override fun onPause() {
        super.onPause()
        //unregister sensor listener
        sensorManager.unregisterListener(this)
        //remove location updates
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    private fun checkForStepCounterSensor() {
        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null) {
            stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
            Log.i("TAG", "Sensor found")
        } else {
            Log.i("TAG", "No sensor available")
            //TODO disable sensor activity
        }
    }

    private fun startLocationUpdates() {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        Log.i("route", "Location request created")

        try {
            val client = LocationServices.getSettingsClient(this.requireActivity())
            val task = client.checkLocationSettings(builder.build())

            //On success
            task.addOnSuccessListener {
                locationUpdateState = true
                startLocationUpdates()
            }

            //On failure
            task.addOnFailureListener { e ->
                if (e is ResolvableApiException) {
                    Log.i("route", "Error in location settings")
                } else {
                    Log.e("route", "CheckLocationSettings task failed: "+e.message)
                }
            }
        } catch (e: Error){
            Log.e("route", "Error getting location updates: ${e.message}")
        }
    }

    fun resetStepCounter() {
        firstStep = true
    }
}