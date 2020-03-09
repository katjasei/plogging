package com.example.plogging.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.plogging.R
import kotlinx.android.synthetic.main.fragment_welcome.*

class NotRegisteredFragment: Fragment() {

    private var activityCallBack: NotRegisteredFragmentListener? = null
    interface NotRegisteredFragmentListener {
        fun onButtonFirstScreenClick()
    }
    override fun onAttach(context: Context)   {
        super.onAttach(context)
        activityCallBack =  context as NotRegisteredFragmentListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_not_registered, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //button Start Plogging clicked
        btn_start_plogging.setOnClickListener {
            activityCallBack!!.onButtonFirstScreenClick()
        }
    }
}