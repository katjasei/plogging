package com.example.plogging.ui.home


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.plogging.R
import kotlinx.android.synthetic.main.fragment_after_stop_activity.*


class AfterStopActivityFragment: Fragment(){

    private var activityCallBack: AfterStopActivityListener? = null

    interface AfterStopActivityListener {
        fun onButtonUploadClick()
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

        btn_upload.setOnClickListener {
            activityCallBack!!.onButtonUploadClick()
        }



//        var points = value_pet_bottles.text.toString().toInt()+value_iron_cans.text.toString().toInt()+value_cardboard.text.toString().toInt()+value_cigarettes.text.toString().toInt()+value_other.text.toString().toInt()

    }
}