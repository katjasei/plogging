package com.example.plogging.ui.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.plogging.ui.home.MainActivity
import com.example.plogging.R
import com.example.plogging.utils.PreferenceHelper
import com.example.plogging.utils.PreferenceHelper.password
import com.example.plogging.utils.PreferenceHelper.userEmail
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment: Fragment() {

    //VARIABLES:
    //firebase auth object
    private var mFirebaseAuth = FirebaseAuth.getInstance()

    //FUNCTIONS:
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_login, container, false)
        val buttonLogin = view.findViewById<Button>(R.id.btn_sign_in)
        val valueEmail = view.findViewById<TextView>(R.id.value_email)
        val valuePassword = view.findViewById<TextView>(R.id.value_password)

        // after button sign in is clicked
        buttonLogin.setOnClickListener {
            //function login
            userLogin(valueEmail.text.toString(), valuePassword.text.toString())
        }
        //using the preferences from PreferenceHelper
        val prefs = PreferenceHelper.customPreference(context!!, "prefs")
        //get user email and password from Shared Preferences
        valueEmail.text = prefs.userEmail
        valuePassword.text = prefs.password
        return view
    }

    @SuppressLint("SetTextI18n")
    // user login through Firebase
    private fun userLogin(email:String, password:String){
        //check if email and password fields are not empty
        if (value_email.text.toString().isNotEmpty() && value_password.text.toString().isNotEmpty()) {
            //logging in the user
            mFirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        activity!!.finish()
                        //move to MainActivity (HomeActivity)
                        val intent = Intent(this.context, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        txt_login_fail.visibility = View.VISIBLE
                        txt_login_fail.text = "Email or password is incorrect"
                    }
                }
        } else {
            //check if user email and password are entered
            if (value_email.text.toString().isEmpty()){
                Toast.makeText(this.context,"Please enter email", Toast.LENGTH_LONG).show()
            }
            if(value_password.text.toString().isEmpty()){
                Toast.makeText(this.context,"Please enter password", Toast.LENGTH_LONG).show()
            }
        }
    }
}
