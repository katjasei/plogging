package com.example.plogging.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.plogging.R
import kotlinx.android.synthetic.main.fragment_point.*

class PointFragment: Fragment(){

    private var activityCallBack: PointActivityListener? = null

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
        return inflater.inflate(R.layout.fragment_point, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val points = arguments!!.getCharSequence("points")
        value_points_profile.text = points
        btn_go_to_profile.setOnClickListener{
            activityCallBack!!.onButtonGoToProfileClick()
        }

    }
}