package com.example.plogging.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.plogging.R
import kotlinx.android.synthetic.main.fragment_no_internet.*

class NoInternetFragment: Fragment() {

    private var activityCallback: NoInternetFragmentListener? = null

    interface NoInternetFragmentListener {
        fun reloadWeather()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_no_internet, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityCallback = context as NoInternetFragmentListener
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //try again button
        tryAgainButton.setOnClickListener{
            activityCallback!!.reloadWeather()
        }
    }
}