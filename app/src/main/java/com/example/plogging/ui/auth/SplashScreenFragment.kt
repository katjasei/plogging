package com.example.plogging.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.DialogFragment
import com.example.plogging.R
import kotlinx.android.synthetic.main.fragment_splash_screen.*

class SplashScreenFragment: DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.actionBar?.hide()
        val animation = AnimationUtils.loadAnimation(this.context,
            R.anim.fade_in
        )

        //set the animation for imageView
        background_image.startAnimation(animation)
    }

}