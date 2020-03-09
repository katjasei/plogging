package com.example.plogging.ui.home


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.plogging.R
import com.example.plogging.data.model.ClassTrash
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_after_stop_activity.*
import java.lang.Integer.parseInt


class AfterStopActivityFragment: Fragment(){

    private var mFirebaseDB = FirebaseDatabase.getInstance().reference
    private var activityCallBack: AfterStopActivityListener? = null

    interface AfterStopActivityListener {
        fun onButtonUploadClick(points:String)
        fun getRoute():MutableList<LatLng>
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityCallBack = context as AfterStopActivityListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_after_stop_activity, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btn_plogging_result.setOnClickListener {
            val points = parseInt(value_pet_bottles.text.toString()) + parseInt(value_iron_cans.text.toString()) + parseInt(value_cardboard.text.toString()) + parseInt(value_cigarettes.text.toString())+ parseInt(value_other.text.toString())
            activityCallBack!!.onButtonUploadClick("+ $points Points")
            val route = activityCallBack!!.getRoute()
            addTrashToDB(points)
            addRouteToDB(route)
        }
    }

    override fun onResume() {
        super.onResume()
        value_pet_bottles.setText("0")
        value_iron_cans.setText("0")
        value_cardboard.setText("0")
        value_cigarettes.setText("0")
        value_other.setText("0")
    }

    private fun addTrashToDB(points:Int){
        val userID = FirebaseAuth.getInstance().currentUser?.uid
        val trash = ClassTrash(
            parseInt(value_pet_bottles.text.toString()),
            parseInt(value_iron_cans.text.toString()),
            parseInt(value_cardboard.text.toString()),
            parseInt(value_cigarettes.text.toString()),
            parseInt(value_other.text.toString()),
            points
        )

        mFirebaseDB.child("users")
            .child(userID!!)
            .child("trash")
            .push()
            .setValue(trash)
    }

    private fun addRouteToDB(route: MutableList<LatLng>){
        val userID = FirebaseAuth.getInstance().currentUser?.uid

        if (route.size != 0) {
            mFirebaseDB.child("users")
                .child(userID!!)
                .child("routes")
                .push()
                .setValue(route)
            Log.i("database", "Route upload successful! Uploaded: $route")
        } else {
            Log.e("database", "Route was empty, not saved to database")
        }
    }
}