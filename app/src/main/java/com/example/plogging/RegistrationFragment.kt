package com.example.plogging

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_registration.*
import org.jetbrains.anko.doAsync

class RegistrationFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btn_sign_up.setOnClickListener {

            val db = UserDB.get(context!!)

            doAsync {

                Log.d("Work", "work")
                db.userDao().insert(User( 0,value_user_name.text.toString(), value_password.text.toString()))

                val data = db.userDao().getAll()
                for(i in 0..(data.size-1))
                Log.d("Data base data", data[i].username)

            }

        }
    }
}

