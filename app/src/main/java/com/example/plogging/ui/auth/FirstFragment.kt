package com.example.plogging.ui.auth

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.plogging.R
import kotlinx.android.synthetic.main.fragment_first.*

class FirstFragment: Fragment() {

    private var activityCallBack: FirstFragmentListener? = null

    interface FirstFragmentListener {
        fun onButtonSignUpClick()
        fun onButtonSignInClick()
    }

    override fun onAttach(context: Context)   {
        super.onAttach(context)
        activityCallBack =  context as FirstFragmentListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btn_sign_up.setOnClickListener {
            activityCallBack!!.onButtonSignUpClick()
        }

        btn_sign_in.setOnClickListener {
            activityCallBack!!.onButtonSignInClick()
        }
    }

}