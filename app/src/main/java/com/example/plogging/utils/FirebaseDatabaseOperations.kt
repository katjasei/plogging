package com.example.plogging.utils

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plogging.R
import com.example.plogging.adapters.TrashAdapter
import com.example.plogging.data.model.ClassTrash
import com.example.plogging.data.model.ClassUser
import com.example.plogging.data.model.UnitTrash
import com.example.plogging.ui.home.ProfileFragment
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.net.URL

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


// function for getting user name from database

fun getUserNameFromDataBase(userID:String, textView:TextView){

    mFirebaseDB.child("users")
        .child(userID!!)
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

    fun getTotalPointsFromDataBase(userID:String, textView:TextView){
    var total = 0
    mFirebaseDB.child("users")
        .child(userID!!)
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

    fun getUnitTrashInfoFromDataBase(userID: String, recyclerView:RecyclerView, context:Context){
     trashUnitList.clear()
    mFirebaseDB.child("users")
        .child(userID!!)
        .addValueEventListener(object: ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(p0: DataSnapshot) {
                var totalPB = 0
                var totalIC = 0
                var totalCB = 0
                var totalC = 0
                var totalO = 0

                if(p0.child("profile_image").value != null)
                    if (p0.child("trash").value != null) {
                        val trash = p0.child("trash").children
                        trash.forEach{
                            totalPB += Integer.parseInt(it.child("pet_bottles").value.toString())
                            totalIC += Integer.parseInt(it.child("iron_cans").value.toString())
                            totalCB += Integer.parseInt(it.child("cardboard").value.toString())
                            totalC += Integer.parseInt(it.child("cigarettes").value.toString())
                            totalO += Integer.parseInt(it.child("other").value.toString())
                        }
                        trashUnitList.add(UnitTrash(R.drawable.pet_bottles,"PET Bottles", totalPB.toString()))
                        trashUnitList.add(UnitTrash(R.drawable.iron_cans,"Iron cans", totalIC.toString()))
                        trashUnitList.add(UnitTrash(R.drawable.cardboard,"Cardboard", totalCB.toString()))
                        trashUnitList.add(UnitTrash(R.drawable.cigarettes,"Cigarettes", totalC.toString()))
                        trashUnitList.add(UnitTrash(R.drawable.other,"Other", totalO.toString()))
                    }
                val trashAdapter = TrashAdapter(trashUnitList)
                recyclerView.layoutManager = LinearLayoutManager(context)
                recyclerView.adapter = trashAdapter
                trashAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(p0: DatabaseError) {
                // Failed to read value
                Log.d("Failed to read value.", "")
            }
        }
        )

}


