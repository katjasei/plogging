package com.example.plogging.ui.auth


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.basgeekball.awesomevalidation.utility.RegexTemplate
import com.example.plogging.R
import com.example.plogging.data.model.ClassUser
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_registration.*


class RegistrationFragment: Fragment() {

    //firebase auth object
    private lateinit var mAuth: FirebaseAuth
    //firebase db
    private var mFirebaseDB = FirebaseDatabase.getInstance().reference

    //callback
    private var activityCallBack: RegistrationFragmentListener? = null

    interface RegistrationFragmentListener {
        fun onButtonSignUpClickFromRegistration(username: String)
    }

    override fun onAttach(context: Context)   {
        super.onAttach(context)
        activityCallBack =  context as RegistrationFragmentListener
    }

    //AwesomeValidation - implement validation for Android
    private var mAwesomeValidation = AwesomeValidation(ValidationStyle.COLORATION)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mAuth = FirebaseAuth.getInstance()

        //validation for all fields
        mAwesomeValidation.setColor(Color.parseColor("#52C7B8"))
        //check if the name has an empty value
        mAwesomeValidation.addValidation(this.activity,
            R.id.value_user_name, RegexTemplate.NOT_EMPTY,
            R.string.invalid_name
        )
        //check that email is valid
        mAwesomeValidation.addValidation(this.activity,
            R.id.value_email, Patterns.EMAIL_ADDRESS,
            R.string.invalid_email
        )
        //check that password is valid
        val regexPassword ="(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}"
        mAwesomeValidation.addValidation(this.activity,
            R.id.value_password,regexPassword,
            R.string.invalid_password
        )
        //password confirmation
        mAwesomeValidation.addValidation(this.activity,
            R.id.value_confirm_password,
            R.id.value_password,
            R.string.invalid_confirm_password
        )

        //when user enters username -> onFocusChange event
        value_user_name.setOnFocusChangeListener { _, hasFocus ->
            txt_duplicate_name.visibility = View.INVISIBLE
            if(!hasFocus && value_user_name.text.toString()!=""){
            userNameExists(value_user_name.text.toString())

            }
        }

        //when user enters email -> onFocusChange event
        value_email.setOnFocusChangeListener { _, hasFocus ->
            txt_duplicate_email.visibility = View.INVISIBLE
            if(!hasFocus && value_email.text.toString()!=""){
                userEmailExists(value_email.text.toString())
            }
        }

        btn_sign_up.setOnClickListener {

            //check if all fields are valid
            if (mAwesomeValidation.validate()) {

            //create user in Firebase
                createUserAccount(
                    value_email.text.toString(),
                    value_password.text.toString()
                )}else

            {
                Snackbar.make(
                    view!!,
                    "You need to change registration information",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun createUserAccount(email:String, password:String){

        //this method create user account with specific email and password
        mAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener{task ->
                if(task.isSuccessful) {
                    //user account created successfully
                    Log.d("Account", "created")
                    addUserNameToUser(task.result?.user!!)
                    activityCallBack!!.onButtonSignUpClickFromRegistration(value_user_name.text.toString())
                }
                else {
                    //account creation failed
                    Log.d("Account", "is not created")
                }
            }
    }

     private fun addUserNameToUser(userFromRegistration: FirebaseUser){

       val username = value_user_name.text.toString()
       val email = userFromRegistration.email
       val userId = userFromRegistration.uid

       val user = ClassUser(username, email!!)

         mFirebaseDB.child("users")
             .child(userId)
             .setValue(user)
     }

    private fun userEmailExists(email:String){
        mFirebaseDB.child("users")
            .orderByChild("email")
            .equalTo(email)
            .addListenerForSingleValueEvent(object: ValueEventListener {

                @SuppressLint("SetTextI18n")
                override fun onDataChange(p0: DataSnapshot) {
                    Log.d("p0",p0.toString())
                 if (p0.exists()){
                         txt_duplicate_email.visibility = View.VISIBLE
                         txt_duplicate_email.text = "$email already in use"

                 } else {
                     txt_duplicate_email.text = ""
                 }
                }
                override fun onCancelled(p0: DatabaseError) {
                }
            })
    }

    private fun userNameExists(username:String){
        mFirebaseDB.child("users")
            .orderByChild("username")
            .equalTo(username)
            .addListenerForSingleValueEvent(object: ValueEventListener {
                @SuppressLint("SetTextI18n")
                override fun onDataChange(p0: DataSnapshot) {
                    Log.d("p0",p0.toString())
                    if (p0.exists()){
                        txt_duplicate_name.visibility = View.VISIBLE
                        txt_duplicate_name.text = "$username already in use"

                    } else {
                        txt_duplicate_name.text = ""
                    }
                }
                override fun onCancelled(p0: DatabaseError) {
                }
            })
    }


}

