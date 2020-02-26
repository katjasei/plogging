package com.example.plogging.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.plogging.R
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_home.*
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker

class HomeFragment: Fragment() {

    var mFirebaseDB =  FirebaseDatabase.getInstance().reference
//    var homeActivity = HomeActivity()
    private var activityCallBack: HomeFragmentListener? = null

    private lateinit var currentLocationMarker: Marker
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    interface HomeFragmentListener {
        fun onButtonLogOutClick()
    }

    override fun onAttach(context: Context)   {
        super.onAttach(context)
        activityCallBack =  context as HomeFragmentListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val userID = FirebaseAuth.getInstance().currentUser?.uid

        mFirebaseDB.child("users")
            .child(userID!!)
            .addValueEventListener(object: ValueEventListener {
                @SuppressLint("SetTextI18n")
                override fun onDataChange(p0: DataSnapshot) {
                    //val user = p0.getValue(ClassUser::class.java)
                    val username = p0.child("username").value.toString()
                    Snackbar.make(
                        view!!,
                        "Welcome, $username",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
                override fun onCancelled(p0: DatabaseError) {
                    // Failed to read value
                    Log.d("Failed to read value.", "")
                }
            })

        //if user click button "LogOut" they moved to FirstScreen
        btn_logout.setOnClickListener {
            activityCallBack!!.onButtonLogOutClick()
        }

        //homeActivity.setActionBarTitle("My jogging record")

        map.setTileSource(TileSourceFactory.MAPNIK)
        map.zoomController
        map.setMultiTouchControls(true)
        map.controller.setZoom(18.0)

        currentLocationMarker = Marker(map)

        //create FusionProviderClient

        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(activity!!)

        buildLocationRequest()

        locationCallback = object : LocationCallback() {

            override fun onLocationResult(locationResult: LocationResult?) {

                locationResult ?: return
                // Update UI with location data
                // ...

                Log.d(
                    "GEOLOCATION",
                    "latitude: ${locationResult.lastLocation?.latitude} and longitude: ${locationResult.lastLocation?.longitude}"
                )

                Log.d("Location", locationResult.lastLocation.toString())
                map.controller.setCenter(GeoPoint(locationResult.lastLocation.latitude, locationResult.lastLocation.longitude))
                //textView2.text = "Latitude:" + locationResult.lastLocation?.latitude + ", Longitude:" + locationResult.lastLocation?.longitude
                currentLocationMarker.position = GeoPoint(locationResult.lastLocation.latitude, locationResult.lastLocation.longitude)
                currentLocationMarker.title = "Latitude: ${locationResult.lastLocation.latitude}\nLongitude: ${locationResult.lastLocation.longitude}"
                currentLocationMarker.icon = getDrawable(context!!, R.drawable.ic_person_pin_circle_blue_a700_24dp)
                if(map.overlays.isNotEmpty()) {
                    map.overlays.clear()
                }
                map.overlays.add(currentLocationMarker)

            }
        }

    }

    private fun buildLocationRequest(){
        locationRequest = LocationRequest().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 5000
            fastestInterval = 3000
            //smallestDisplacement = 10f

        }
    }

    override fun onStart() {
        super.onStart()
        startLocationUpdates()
    }

    private fun startLocationUpdates() {
        if ((Build.VERSION.SDK_INT >= 23 &&
                    ContextCompat.checkSelfPermission(
                        this.context!!,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ) !=
                    PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                activity!!,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                0
            )
        }
        fusedLocationClient.requestLocationUpdates(locationRequest,
            locationCallback,
            null)
    }



}
