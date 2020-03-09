package com.example.plogging.ui.home


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.plogging.R
import com.example.plogging.data.model.ClassTrash
import com.example.plogging.utils.addRouteToDB
import com.example.plogging.utils.addTrashToDB
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_after_stop_activity.*
import java.lang.Integer.parseInt


class AfterStopActivityFragment: Fragment(){

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
        //when user enter the number of gathered trash, function onFocus Change clean text value
        onFocusChange(value_pet_bottles)
        onFocusChange(value_iron_cans)
        onFocusChange(value_cardboard)
        onFocusChange(value_cigarettes)
        onFocusChange(value_other)

        btn_plogging_result.setOnClickListener {
            val points = parseInt(value_pet_bottles.text.toString()) + parseInt(value_iron_cans.text.toString()) + parseInt(value_cardboard.text.toString()) + parseInt(value_cigarettes.text.toString())+ parseInt(value_other.text.toString())
            activityCallBack!!.onButtonUploadClick("+ $points Points")
            val route = activityCallBack!!.getRoute()
            addTrashToDB(points,value_pet_bottles, value_iron_cans, value_cardboard, value_cigarettes, value_other)
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

    private fun onFocusChange(editText: EditText){
        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus){
                editText.setText("")
            }
        }
    }
}