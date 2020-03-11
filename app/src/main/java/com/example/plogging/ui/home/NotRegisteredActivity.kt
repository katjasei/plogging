package com.example.plogging.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.plogging.R
import com.example.plogging.ui.auth.AuthActivity
import kotlinx.android.synthetic.main.fragment_not_registered.*


class NotRegisteredActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_not_registered)
        btn_registration.setOnClickListener {
          val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
        }
    }
}