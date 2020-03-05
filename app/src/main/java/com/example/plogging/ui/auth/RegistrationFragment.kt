package com.example.plogging.ui.auth

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.basgeekball.awesomevalidation.utility.RegexTemplate
import com.example.plogging.R
import com.example.plogging.utils.PreferenceHelper.customPreference
import com.example.plogging.utils.PreferenceHelper.password
import com.example.plogging.utils.PreferenceHelper.userEmail
import com.example.plogging.utils.PreferenceHelper.clearValues
import com.example.plogging.utils.addUserNameToUser
import com.example.plogging.utils.checkIfParameterExistInFirebaseDB
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_registration.*


class RegistrationFragment: Fragment() {

    //VARIABLES:
    //firebase auth object
    private  var mAuth = FirebaseAuth.getInstance()
    //AwesomeValidation - implement validation for Android
    private var mAwesomeValidation = AwesomeValidation(ValidationStyle.COLORATION)
    //callback variable, interface and onAttach fun
    private var activityCallBack: RegistrationFragmentListener? = null

    //INTERFACES AND FUNCTIONS:
    interface RegistrationFragmentListener {
        fun onButtonSignUpClickFromRegistration(username: String)
    }
    override fun onAttach(context: Context)   {
        super.onAttach(context)
        activityCallBack =  context as RegistrationFragmentListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //VALIDATION for all fields
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
        onFocusChangedListener(value_user_name,txt_duplicate_name)
        //when user enters email -> onFocusChange event
        onFocusChangedListener(value_email,txt_duplicate_email)

        btn_sign_up.setOnClickListener {
            //using the preferences from PreferenceHelper
            val prefs = customPreference(context!!, "prefs")

            //check if all fields are valid
            if (mAwesomeValidation.validate()) {
              //clear old values in "prefs"
              prefs.clearValues
              //create user in Firebase
              //save user password and email to SharedPreferences
                prefs.password = value_password.text.toString()
                prefs.userEmail = value_email.text.toString()
                createUserAccount(
                    value_email.text.toString(),
                    value_password.text.toString()
                )} else {
                Snackbar.make(
                    view!!,
                    "You need to change registration information",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    //this method create user account in Firebase with specific email and password
    private fun createUserAccount(email:String, password:String){
        mAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener{task ->
                if(task.isSuccessful) {
                    //user account created successfully
                    addUserNameToUser(task.result?.user!!, value_user_name)
                    activityCallBack!!.onButtonSignUpClickFromRegistration(value_user_name.text.toString())
                }
                else {
                    //account creation failed
                    Log.d("Account", "is not created")
                }
            }
    }

    private fun userEmailExists(email:String){
     checkIfParameterExistInFirebaseDB(email, "email", txt_duplicate_email)
    }

    private fun userNameExists(username:String){
      checkIfParameterExistInFirebaseDB(username, "username", txt_duplicate_name)
    }

    private fun onFocusChangedListener(textView: TextView, textViewMessage: TextView){
        textView.setOnFocusChangeListener { _, hasFocus ->
            textViewMessage.visibility = View.INVISIBLE
            if(!hasFocus && textView.text.toString()!=""){
                if (textView==value_user_name){
                userNameExists(textView.text.toString())}
                else{
                userEmailExists(textView.text.toString())
                }

            }
        }
    }




}

