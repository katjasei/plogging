package com.example.plogging.ui.home


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.plogging.R



class AfterStopActivityFragment: Fragment(){

/*
    private var activityCallBack: PloggingActivityListener? = null

    interface PloggingActivityListener {
        fun onButtonStopActivityClick()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityCallBack = context as PloggingActivityListener
    }

*/

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
/*
        btn_stop_activity.setOnClickListener {
            activityCallBack!!.onButtonStopActivityClick()
        }

 */
    }
}