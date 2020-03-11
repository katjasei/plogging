package com.example.plogging.ui.home


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.plogging.R
import com.example.plogging.data.model.ClassRoute
import com.example.plogging.data.model.ClassTrash
import com.example.plogging.utils.addRouteToDB
import com.example.plogging.utils.addTrashToDB
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_after_stop_activity.*
import java.lang.Integer.parseInt


class AfterStopActivityFragment: Fragment() {

    private var activityCallBack: AfterStopActivityListener? = null

    interface AfterStopActivityListener {
        fun onButtonUploadClick(points: String)
        fun getRoute(): MutableList<LatLng>
        fun getRouteLength(): Double
        fun getRouteTime(): Int
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
            //if user don't enter number of gathered trash, by default it is 0
            checkTrashUnitValue(value_pet_bottles)
            checkTrashUnitValue(value_iron_cans)
            checkTrashUnitValue(value_cardboard)
            checkTrashUnitValue(value_cigarettes)
            checkTrashUnitValue(value_other)

            val points =
                parseInt(value_pet_bottles.text.toString()) + parseInt(value_iron_cans.text.toString()) + parseInt(
                    value_cardboard.text.toString()
                ) + parseInt(value_cigarettes.text.toString()) + parseInt(value_other.text.toString())
            activityCallBack!!.onButtonUploadClick("+ $points Points")

            val route = activityCallBack!!.getRoute()
            val distance = activityCallBack!!.getRouteLength()
            val time = activityCallBack!!.getRouteTime()
            addRouteToDB(distance, route, time)

            addTrashToDB(
                points,
                value_pet_bottles,
                value_iron_cans,
                value_cardboard,
                value_cigarettes,
                value_other
            )
        }
    }

    override fun onResume() {
        super.onResume()
        value_pet_bottles.setText("")
        value_iron_cans.setText("")
        value_cardboard.setText("")
        value_cigarettes.setText("")
        value_other.setText("")
    }

    private fun checkTrashUnitValue(editText: EditText) {
        if (editText.text.toString() == "") {
            editText.setText("0")
        }
    }
}