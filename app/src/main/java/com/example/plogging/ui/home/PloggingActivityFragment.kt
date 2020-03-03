package com.example.plogging.ui.home


import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_plogging_activity.*
import kotlinx.android.synthetic.main.fragment_plogging_activity.floating_action_button


class PloggingActivityFragment: Fragment(), OnMapReadyCallback {


    private var activityCallBack: PloggingActivityListener? = null

    interface PloggingActivityListener {
        fun onButtonStopActivityClick()
    }

    override fun onAttach(context: Context)   {
        super.onAttach(context)
        activityCallBack =  context as PloggingActivityListener
    }


    private lateinit var  fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var currentLocation: LatLng
    //private lateinit var mMap: GoogleMap

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
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
    }

    override fun onMapReady(map: GoogleMap) {
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location ->
             currentLocation = LatLng(location.latitude, location.longitude)

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