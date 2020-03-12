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
import com.example.plogging.data.model.*
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
    var listOfTrash1: MutableList<ClassUserTrash> = java.util.ArrayList()

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
fun addRouteToDB(distance: Double, route: MutableList<LatLng>, time: Int) {

    val userID = FirebaseAuth.getInstance().currentUser?.uid
    val finalRoute = ClassRoute(distance, route, time)

    if (route.size > 1) {
        mFirebaseDB.child("users")
            .child(userID!!)
            .child("routes")
            .push()
            .setValue(finalRoute)
        Log.i("database", "Route upload succesful! Uploaded: " + finalRoute)
        Log.i("database", "Time uploaded: " + finalRoute.time)
    } else {
        Log.e("database", "Route was empty or other error, not saved to database")
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
       var total: Int
       mFirebaseDB.child("users")
        .child(userID)
        .addValueEventListener(object:ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                total = 0
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

fun getTotalDistanceFromDatabase(userID: String, textView: TextView) {
    var totalDistance: Double
    mFirebaseDB.child("users")
        .child(userID)
        .addValueEventListener(object:ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                totalDistance = 0.0
                p0.child("routes").children.forEach {
                    totalDistance += it.child("distance").value.toString().toDouble()
                }
                textView.text = "%.2f".format(totalDistance)
            }

            override fun onCancelled(p0: DatabaseError) {
                // Failed to read value
                Log.d("Failed to read value.", "")
            }
        })
}

fun getTotalTimeFromDatabase(userID: String, textView: TextView){
    var totalTime: Int
    mFirebaseDB.child("users")
        .child(userID)
        .addValueEventListener(object:ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                totalTime = 0
                p0.child("routes").children.forEach {
                    if (it.child("time").value.toString().toInt() > 0)
                        totalTime += it.child("time").value.toString().toInt()
                }
                when (totalTime) {
                    in 0..60 -> textView.text = totalTime.toString()+"s"
                    in 61..3600 -> textView.text = (totalTime/60).toString()+"min"
                    else -> textView.text = (totalTime/3600).toString()+"h"
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                // Failed to read value
                Log.d("Failed to read value.", "")
            }
        })
}


fun getUsersAndTotalPointsFromDB(recyclerView:RecyclerView, context:Context){

    mFirebaseDB.child("users")
        .addValueEventListener(object: ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(p0: DataSnapshot) {
                listOfTrash1.clear()
                val children = p0.children
                var total = 0
                var username: String
                children.forEach { it ->
                    if (it.child("trash").value != null) {
                        username = it.child("username").value.toString()
                        val child = it.child("trash")
                        child.children.forEach{
                            total += Integer.parseInt(it.child("total").value.toString())
                        }
                        if (total != 0){
                            listOfTrash1.add(ClassUserTrash(username, total))}
                        total = 0
                    }
                    val leaderBoardAdapter = LeaderBoardAdapter(listOfTrash1.sortedByDescending{ it.trashTotal })
                    recyclerView.layoutManager = LinearLayoutManager(context)
                    recyclerView.adapter = leaderBoardAdapter
                    leaderBoardAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                // Failed to read value
                Log.d("Failed to read value.", "")
            }
        }
        )
}

fun getUnitTrashInfoForUser(userID:String, recyclerViewTrash:RecyclerView,context: Context){
    mFirebaseDB.child("users")
        .child(userID)
        .addValueEventListener(object: ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(p0: DataSnapshot) {
                var totalPB = 0
                var totalIC = 0
                var totalCB = 0
                var totalC = 0
                var totalO = 0
                trashUnitList.clear()
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
                recyclerViewTrash.layoutManager = LinearLayoutManager(context)
                recyclerViewTrash.adapter = trashAdapter
                trashAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(p0: DatabaseError) {
                // Failed to read value
                Log.d("Failed to read value.", "")
            }
        }
        )
}


