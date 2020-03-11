package com.example.plogging.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.plogging.R

class PointFragment: Fragment(){

    //VARIABLES:
    private var activityCallBack: PointActivityListener? = null

    //FUNCTIONS AND INTERFACES:
    interface PointActivityListener {
        fun onButtonGoToProfileClick()
    }
    override fun onAttach(context: Context)   {
        super.onAttach(context)
        activityCallBack =  context as PointActivityListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_point, container, false)
        val valuePoints = view.findViewById<TextView>(R.id.value_points_profile)
        val buttonGoToProfile = view.findViewById<Button>(R.id.btn_go_to_profile)
        val points = arguments!!.getCharSequence("points")
        valuePoints.text = points
        buttonGoToProfile.setOnClickListener{
            activityCallBack!!.onButtonGoToProfileClick()
        }
        return view
    }
}