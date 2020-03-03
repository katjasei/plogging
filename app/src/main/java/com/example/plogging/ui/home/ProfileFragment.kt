package com.example.plogging.ui.home

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.plogging.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_profile.*
import java.net.URI


class ProfileFragment: Fragment(){

    var mFirebaseDB =  FirebaseDatabase.getInstance().reference
     val REQUESTCODE = 1
    lateinit var pickedImageURI: Uri

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        //val REQUEST_IMAGE_CAPTURE = 100
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val usernameTextView = view.findViewById<TextView>(R.id.value_user_name_profile)
        val totalPoints = view.findViewById<TextView>(R.id.value_points_profile)
        val totalPet = view.findViewById<TextView>(R.id.value_pet_bottles)
        val totalCans = view.findViewById<TextView>(R.id.value_iron_cans)
        val totalCardBoard = view.findViewById<TextView>(R.id.value_cardboard)
        val totalCigarettes = view.findViewById<TextView>(R.id.value_cigarettes)
        val totalOther = view.findViewById<TextView>(R.id.value_other)
        val userID = FirebaseAuth.getInstance().currentUser?.uid

        mFirebaseDB.child("users")
            .child(userID!!)
            .addValueEventListener(object: ValueEventListener {
                @SuppressLint("SetTextI18n")
                override fun onDataChange(p0: DataSnapshot) {

                    var total = 0
                    var totalPB = 0
                    var totalIC = 0
                    var totalCB = 0
                    var totalC = 0
                    var totalO = 0
                    var username = ""

                        Log.d("p0.value", p0.value.toString())

                            username = p0.child("username").value.toString()
                            usernameTextView.text = username
                        if (p0.child("trash").value != null) {
                        val trash = p0.child("trash").children
                            trash.forEach{
                                total += Integer.parseInt(it.child("total").value.toString())
                                totalPB += Integer.parseInt(it.child("pet_bottles").value.toString())
                                totalIC += Integer.parseInt(it.child("iron_cans").value.toString())
                                totalCB += Integer.parseInt(it.child("cardboard").value.toString())
                                totalC += Integer.parseInt(it.child("cigarettes").value.toString())
                                totalO += Integer.parseInt(it.child("other").value.toString())
                                Log.d("Total1", it.child("total").value.toString())
                            }
                            Log.d("Total", total.toString())
                            totalPoints.text = total.toString()
                            totalPet.text = totalPB.toString()
                            totalCans.text = totalIC.toString()
                            totalCardBoard.text = totalCB.toString()
                            totalCigarettes.text = totalC.toString()
                            totalOther.text = totalO.toString()
                    }
                }
                override fun onCancelled(p0: DatabaseError) {
                    // Failed to read value
                    Log.d("Failed to read value.", "")
                }
            }
            )

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profile_image.setOnClickListener {
            openGallery()
        }
    }

    private fun openGallery(){
        //open gallery intent and wait for user to pick an image
        val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
        galleryIntent.type = "image/*"
        startActivityForResult(galleryIntent, REQUESTCODE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == RESULT_OK && requestCode ==REQUESTCODE && data != null ){

            //the user has successfully picked an image
            //we need to save its reference to a URI variable
            pickedImageURI = data.data!!

            profile_image.setImageURI(pickedImageURI)

        }

    }

}