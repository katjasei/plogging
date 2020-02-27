package com.example.plogging.ui.auth

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.plogging.R
import kotlinx.android.synthetic.main.fragment_welcome.*

class WelcomeFragment: Fragment() {

    private var activityCallBack: WelcomeFragmentListener? = null

    interface WelcomeFragmentListener {
        fun onButtonStartPloggingClick()
    }

    override fun onAttach(context: Context)   {
        super.onAttach(context)
        activityCallBack =  context as WelcomeFragmentListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // get username from bundle
        val username = arguments!!.getCharSequence("username")
        value_welcome_username.text = username

        //button Start Plogging clicked
        btn_start_plogging.setOnClickListener {
            activityCallBack!!.onButtonStartPloggingClick()
        }
    }
}