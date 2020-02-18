package com.example.plogging


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
import kotlinx.android.synthetic.main.fragment_registration.*
import org.jetbrains.anko.doAsync

class RegistrationFragment: Fragment() {

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

        //new user registration, user data saves in database "User"
        btn_sign_up.setOnClickListener {
            val db = UserDB.get(context!!)

            if (mAwesomeValidation.validate()) {

                doAsync {

                    Log.d("Work", "work")
                    db.userDao().insert(
                        User(
                            0,
                            value_user_name.text.toString(),
                            value_email.text.toString(),
                            value_password.text.toString()
                        )
                    )

                    val data = db.userDao().getAll()
                    /*for(i in 0..(data.size-1))
                Log.d("Data base data", data[i].username)*/
                }
                
                activityCallBack!!.onButtonSignUpClickFromRegistration()
            }


        }
    }
}

