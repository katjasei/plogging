package com.example.plogging.utils

import android.app.Activity
import android.net.Uri
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage

//function for uploading profile image to Firebase DB
fun uploadFileToFirebaseStorage(pickedImageURI:Uri, currentUserID:String, activity:Activity){
    val mStorage = FirebaseStorage.getInstance().reference.child("$currentUserID.jpg")
    val imageFilePath = mStorage.child(pickedImageURI.lastPathSegment!!)

    imageFilePath
        .putFile(pickedImageURI)
        .addOnSuccessListener {
            imageFilePath.downloadUrl.addOnSuccessListener {
                val downloadURL = it.toString()
                mFirebaseDB.child("users")
                    .child(currentUserID)
                    .child("profile_image")
                    .setValue(downloadURL)
            }
            Toast.makeText(activity,"Uploaded", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(activity, "Failed$it", Toast.LENGTH_SHORT).show()
        }
}