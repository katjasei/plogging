package com.example.plogging.ui.home

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.res.Resources
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.plogging.R
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_home.*
import org.osmdroid.views.overlay.Marker
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

class HomeFragment: Fragment(), OnMapReadyCallback  {

    var mFirebaseDB =  FirebaseDatabase.getInstance().reference
    private var activityCallBack: HomeFragmentListener? = null

    private lateinit var  fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var mMap: GoogleMap

    interface HomeFragmentListener {
        fun onButtonLogOutClick()
        fun onButtonStartActivityClick()
    }

    override fun onAttach(context: Context)   {
        super.onAttach(context)
        activityCallBack =  context as HomeFragmentListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
//        HomeActivity().actionBar?.title =  "My jogging record"
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

        //if user click button "StartActivity"
        btn_start_activity.setOnClickListener {
            activityCallBack!!.onButtonStartActivityClick()
        }

        //
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
            val currentLocation = LatLng(location.latitude, location.longitude)
            /* try {
                 val success = map.setMapStyle(
                     MapStyleOptions.loadRawResourceStyle(context,
                         R.raw.style_json
                     )
                 )
                 if (!success) Log.d(TAG, "Style parsing failed.")
             } catch (e: Resources.NotFoundException) {
                 Log.d(TAG, "Can't find style. Error: $e")
             }
 */
            map.addMarker(
                MarkerOptions()
                    .position(currentLocation)
                    .title("Your current location")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            )
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f))
        }
    }





}
