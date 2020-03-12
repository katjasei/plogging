package com.example.plogging.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.example.plogging.R

class SplashScreenFragment: DialogFragment() {

    //FUNCTIONS:
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_splash_screen, container, false)
        val backgroundImage = view.findViewById<ImageView>(R.id.background_image)
        val animation = AnimationUtils.loadAnimation(this.context,
            R.anim.fade_in
        )
        //set the animation for imageView
        backgroundImage.startAnimation(animation)
        return view
    }

}