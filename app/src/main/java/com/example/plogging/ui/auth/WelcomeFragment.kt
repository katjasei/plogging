package com.example.plogging.ui.auth

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.plogging.R


class WelcomeFragment: Fragment() {

    //VARIABLES:
    private var activityCallBack: WelcomeFragmentListener? = null

    //INTERFACES AND FUNCTIONS:
    interface WelcomeFragmentListener {
        fun onButtonStartPloggingClick()
    }
    override fun onAttach(context: Context)   {
        super.onAttach(context)
        activityCallBack =  context as WelcomeFragmentListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_welcome, container, false)
        val valueUserName = view.findViewById<TextView>(R.id.value_welcome_username)
        val buttonStartPlogging = view.findViewById<Button>(R.id.btn_start_plogging)

        // get username from bundle
        val username = arguments!!.getCharSequence("username")
        valueUserName.text = username

        //button Start Plogging clicked
        buttonStartPlogging.setOnClickListener {
            activityCallBack!!.onButtonStartPloggingClick()
        }
        return view
    }
}