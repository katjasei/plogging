package com.example.plogging

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_login.*
import org.jetbrains.anko.doAsync

class LoginFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val db = UserDB.get(context!!)

        doAsync {
        if (db.userDao().getByUserNameAndPassword(value_user_name.toString(),value_password.toString()).isNotEmpty()){

         Log.d("Hello user",db.userDao().getByUserNameAndPassword(value_user_name.toString(),value_password.toString())[0].username )
        }
        }


    }
}
