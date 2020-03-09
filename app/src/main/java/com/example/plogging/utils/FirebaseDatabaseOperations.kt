package com.example.plogging.utils

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.widget.TextView
import com.example.plogging.data.model.ClassTrash
import com.example.plogging.data.model.ClassUser
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

    //VARIABLES:
    //firebase db
    var mFirebaseDB = FirebaseDatabase.getInstance().reference

     //FUNCTIONS:
    //Functions, that are used in RegistrationFragment
    //function checks if username or user_email already in use
    fun checkIfParameterExistInFirebaseDB (parameter:String, parameterName:String, textView: TextView){
        mFirebaseDB.child("users")
            .orderByChild(parameterName)
            .equalTo(parameter)
            .addListenerForSingleValueEvent(object: ValueEventListener {
                @SuppressLint("SetTextI18n")
                override fun onDataChange(p0: DataSnapshot) {
                    Log.d("p0",p0.toString())
                    if (p0.exists()){
                        textView.visibility = View.VISIBLE
                        textView.text = "$parameter already in use"
                    } else {
                        textView.text = ""
                    }
                }
                override fun onCancelled(p0: DatabaseError) {
                }
            })
    }

   //function for adding user to Firebase DB
   fun addUserNameToUser(userFromRegistration: FirebaseUser, textView:TextView){
    val username = textView.text.toString()
    val email = userFromRegistration.email
    val userId = userFromRegistration.uid
    val user = ClassUser(username, email!!)
    mFirebaseDB.child("users")
        .child(userId)
        .setValue(user)
}

    //function for adding route to Firebase DB
    fun addRouteToDB(route: MutableList<LatLng>){
    val userID = FirebaseAuth.getInstance().currentUser?.uid
    if (route.size != 0) {
        mFirebaseDB.child("users")
            .child(userID!!)
            .child("routes")
            .push()
            .setValue(route)
        Log.i("database", "Route upload successful! Uploaded: $route")
    } else {
        Log.e("database", "Route was empty, not saved to database")
    }
}
    //function for adding trash to Firebase DB, in order to count number of points
    // and number of pet_bottles, iron_cans, etc. that user gathered
    fun addTrashToDB(points:Int, pet_textView:TextView, can_textView:TextView, cardboard_textView:TextView,
                         cig_textView:TextView,other_textView:TextView ){
    val userID = FirebaseAuth.getInstance().currentUser?.uid
    val trash = ClassTrash(
        Integer.parseInt(pet_textView.text.toString()),
        Integer.parseInt(can_textView.text.toString()),
        Integer.parseInt(cardboard_textView.text.toString()),
        Integer.parseInt(cig_textView.text.toString()),
        Integer.parseInt(other_textView.text.toString()),
        points
    )
    mFirebaseDB.child("users")
        .child(userID!!)
        .child("trash")
        .push()
        .setValue(trash)
}

