package com.example.plogging

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment: Fragment() {

    //firebase ajuth object
    lateinit var mFirebaseAuth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btn_sign_in.setOnClickListener {
            userLogin(value_email.text.toString(), value_password.text.toString())
        }
    }

    private fun userLogin(email:String, password:String){

        if (value_email.text.toString().isEmpty()){
            Toast.makeText(this.context,"Please enter email", Toast.LENGTH_LONG).show()
        }

        if(value_password.text.toString().isEmpty()){
            Toast.makeText(this.context,"Please enter password", Toast.LENGTH_LONG).show()
        }
        mFirebaseAuth = FirebaseAuth.getInstance()
        //logging in the user
        mFirebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{task ->
                if(task.isSuccessful) {
                    activity!!.finish()
                    val intent = Intent(this.context, HomeActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this.context,"Email or password is incorrect", Toast.LENGTH_LONG).show()
                }
            }
    }
}
