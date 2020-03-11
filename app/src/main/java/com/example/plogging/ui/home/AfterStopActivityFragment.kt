package com.example.plogging.ui.home


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
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


class AfterStopActivityFragment: Fragment(){

    //VARIABLES:
    private var activityCallBack: AfterStopActivityListener? = null

    //FUNCTIONS AND INTERFACES
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
        val view = inflater.inflate(R.layout.fragment_after_stop_activity, container, false)
        val buttonUpload = view.findViewById<Button>(R.id.btn_upload_after_activity)
        val valuePET = view.findViewById<EditText>(R.id.value_pet_bottles)
        val valueCan = view.findViewById<EditText>(R.id.value_iron_cans)
        val valueCardboard = view.findViewById<EditText>(R.id.value_cardboard)
        val valueCig = view.findViewById<EditText>(R.id.value_cigarettes)
        val valueOther = view.findViewById<EditText>(R.id.value_other)
        buttonUpload.setOnClickListener {
            //if user don't enter number of gathered trash, by default it is 0
            checkTrashUnitValue(valuePET)
            checkTrashUnitValue(valueCan)
            checkTrashUnitValue(valueCardboard)
            checkTrashUnitValue(valueCig)
            checkTrashUnitValue(valueOther)
            //all points that user enter
            val points =
                parseInt(valuePET.text.toString()) + parseInt(valueCan.text.toString()) + parseInt(
                    valueCardboard.text.toString()
                ) + parseInt(valueCig.text.toString()) + parseInt(valueOther.text.toString())
            activityCallBack!!.onButtonUploadClick("+ $points Points")
            val route = activityCallBack!!.getRoute()
            val distance = activityCallBack!!.getRouteLength()
            val time = activityCallBack!!.getRouteTime()

            addTrashToDB(
                points,
                valuePET,
                valueCan,
                valueCardboard,
                valueCig,
                valueOther
            )

            addRouteToDB(route)
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        value_pet_bottles.setText("")
        value_iron_cans.setText("")
        value_cardboard.setText("")
        value_cigarettes.setText("")
        value_other.setText("")
    }
    //if no value in Edit text, it value by default 0
    private fun checkTrashUnitValue(editText: EditText) {
            if(editText.text.toString()==""){
                editText.setText("0")
            }
        }
}