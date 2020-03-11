package com.example.plogging.utils

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plogging.R
import com.example.plogging.adapters.LeaderBoardAdapter
import com.example.plogging.adapters.TrashAdapter
import com.example.plogging.data.model.ClassTrash
import com.example.plogging.data.model.ClassUser
import com.example.plogging.data.model.ClassUserTrash
import com.example.plogging.data.model.UnitTrash
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
    var trashUnitList: MutableList<UnitTrash> = java.util.ArrayList()

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

   //function add user to Firebase DB
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


// function for getting user name from database
fun getUserNameFromDataBase(userID:String, textView:TextView){

    mFirebaseDB.child("users")
        .child(userID)
        .addValueEventListener(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                val username = p0.child("username").value.toString()
                textView.text = username
            }
            override fun onCancelled(p0: DatabaseError) {
                // Failed to read value
                Log.d("Failed to read value.", "")
            }
        }
        )
}
   //get information about total number of points that user has
    fun getTotalPointsFromDataBase(userID:String, textView:TextView){
    var total = 0
    mFirebaseDB.child("users")
        .child(userID)
        .addValueEventListener(object:ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.child("trash").value != null) {
                    val trash = p0.child("trash").children
                    trash.forEach{
                        total += Integer.parseInt(it.child("total").value.toString())
                    }
                    textView.text = total.toString()
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                // Failed to read value
                Log.d("Failed to read value.", "")
            }
        })

}
    //get information about all kind of trash (pet_bottles,iron_cans...) that user gathered
    fun getUnitTrashInfoFromDataBase(userID: String, recyclerView:RecyclerView, context:Context){

}


