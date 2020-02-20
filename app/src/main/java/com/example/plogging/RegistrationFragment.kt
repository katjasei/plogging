package com.example.plogging


import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.basgeekball.awesomevalidation.utility.RegexTemplate
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_registration.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.sdk27.coroutines.onFocusChange
import org.jetbrains.anko.uiThread

class RegistrationFragment: Fragment() {

    private lateinit var db: UserDB

    //callback
    private var activityCallBack: RegistrationFragmentListener? = null

    interface RegistrationFragmentListener {
        fun onButtonSignUpClickFromRegistration()
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

        //validation for all fields
        mAwesomeValidation.setColor(Color.parseColor("#52C7B8"))
        //check if the name has an empty value
        mAwesomeValidation.addValidation(this.activity, R.id.value_user_name, RegexTemplate.NOT_EMPTY, R.string.invalid_name)
        //check that email is valid
        mAwesomeValidation.addValidation(this.activity,R.id.value_email, Patterns.EMAIL_ADDRESS,R.string.invalid_email)
        //check that password is valid
        val regexPassword ="(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}"
        mAwesomeValidation.addValidation(this.activity, R.id.value_password,regexPassword, R.string.invalid_password )
        //password confirmation
        mAwesomeValidation.addValidation(this.activity, R.id.value_confirm_password, R.id.value_password, R.string.invalid_confirm_password)

        db = UserDB.get(context!!)

        //when user enters username -> onFocusChange event
        value_user_name.setOnFocusChangeListener { v, hasFocus ->
            txt_duplicate_name.visibility = View.INVISIBLE
            txt_duplicate_name.text = ""
            if(!hasFocus && value_user_name.text.toString()!=""){
                doAsync {
                    if (db.userDao().checkIfUserNameExist(value_user_name.text.toString()).isNotEmpty()) {
                        var userNameExist =
                            db.userDao().checkIfUserNameExist(value_user_name.text.toString())[0].username
                        uiThread {
                            txt_duplicate_name.visibility = View.VISIBLE
                            txt_duplicate_name.text = "User name $userNameExist already in use"

                        }
                    }
                }

            }
        }

        //when user enters email -> onFocusChange event
        value_email.setOnFocusChangeListener { v, hasFocus ->
            txt_duplicate_email.visibility = View.INVISIBLE
            txt_duplicate_email.text = ""
            if(!hasFocus && value_email.text.toString()!=""){
                doAsync {
                    if (db.userDao().checkIfUserEmailExist(value_email.text.toString()).isNotEmpty()) {
                        var userEmailExist =
                            db.userDao().checkIfUserEmailExist(value_email.text.toString())[0].email
                        uiThread {
                            txt_duplicate_email.visibility = View.VISIBLE
                            txt_duplicate_email.text = "Email $userEmailExist already in use"
                        }
                    }
                }

            }
        }

        //new user registration, user data saves in database "User"
        btn_sign_up.setOnClickListener {
            Log.d("Duplicate email",txt_duplicate_email.text.toString() )

            if (mAwesomeValidation.validate() && txt_duplicate_email.text.isBlank() && txt_duplicate_name.text.isBlank()) {
                doAsync {
                    db.userDao().insert(
                        User(
                            0,
                            value_user_name.text.toString(),
                            value_email.text.toString(),
                            value_password.text.toString()
                        )
                    )
                    val data = db.userDao().getAll()
                    for(i in 0..(data.size-1))
                Log.d("Data base data", data[i].username)
                }
                activityCallBack!!.onButtonSignUpClickFromRegistration()
            } else

            {
                Snackbar.make(
                    view!!,
                    "You need to change registration information",
                    Snackbar.LENGTH_LONG
                ).show()
            }

        }

    }

}

