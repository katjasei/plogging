package com.example.plogging.ui.home

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.example.plogging.R
import com.example.plogging.utils.uploadFileToFirebaseStorage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.File
import java.io.InputStream
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL


class ProfileFragment: Fragment(){

    //VARIABLES:
    val REQUEST_IMAGE_CAPTURE = 99
    val REQUESTCODE = 1
    var mCurrentPhotoPath = ""
    var mFirebaseDB =  FirebaseDatabase.getInstance().reference
    val currentUserID = FirebaseAuth.getInstance().currentUser?.uid
    private var activityCallBack: ProfileFragmentListener? = null
    val alertItems = arrayOf("Open camera","Choose from library")

    interface ProfileFragmentListener {
        fun onButtonLogOutClick()
    }
    override fun onAttach(context: Context)   {
        super.onAttach(context)
        activityCallBack =  context as ProfileFragmentListener
    }

    data class  URLparams(val url: URL)
    data class FinalBitmap(val bitmap: Bitmap)

    inner class GetConn : AsyncTask<URLparams, Unit, FinalBitmap>() {
        override fun doInBackground(vararg params: URLparams): FinalBitmap {
            lateinit var result: FinalBitmap
            try {
                val myConn = params[0].url.openConnection() as HttpURLConnection
                val istream: InputStream = myConn.inputStream
                val image = BitmapFactory.decodeStream(istream)
                result = FinalBitmap(image)

            } catch (e: Exception) {
                Log.e("Connection", "Reading error", e)
            }
            return result
        }
        override fun onPostExecute(result: FinalBitmap) {
                profile_image.setImageBitmap(result.bitmap)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
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
                           if(p0.child("profile_image").value != null){
                               if (isNetworkAvailable()){
                                   val myURLparams = URLparams(URL(p0.child("profile_image").value.toString()))
                                   GetConn().execute(myURLparams)
                               }
                           }

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
            showAlertDialog()
        }
        //if user click button "LogOut" they move to FirstScreen
        btn_logout.setOnClickListener{
            activityCallBack!!.onButtonLogOutClick()
        }
    }


    private fun openGallery(){
        //open gallery intent and wait for user to pick an image
        val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
        galleryIntent.type = "image/*"
        startActivityForResult(galleryIntent, REQUESTCODE)
    }

    private fun openCamera(){
        val fileName = "temp_photo"
        val imgPath = context!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        var imageFile: File? = null
        imageFile = File.createTempFile(fileName, ".jpg", imgPath)
        val photoURI: Uri =
            FileProvider.getUriForFile(this.context!!, "com.example.plogging", imageFile)
        mCurrentPhotoPath = imageFile!!.absolutePath
        val myIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (myIntent.resolveActivity(context!!.packageManager) != null) {
            myIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(myIntent, REQUEST_IMAGE_CAPTURE)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode == REQUESTCODE && data != null ){
            //the user has successfully picked an image
            //we need to save its reference to a URI variable
            val pickedImageURI = data.data!!
            profile_image.setImageURI(pickedImageURI)
            //upload user photo to firebase storage and get url
            uploadFileToFirebaseStorage(pickedImageURI, currentUserID!!, activity!!)
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath)
            uploadFileToFirebaseStorage(Uri.fromFile(File(mCurrentPhotoPath)), currentUserID!!, activity!!)
            profile_image.setImageBitmap(imageBitmap)
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = activity!!.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        //if activeNetworkInfo == false -> if isConnected == false -> return false
        return connectivityManager.activeNetworkInfo?.isConnected?:false
    }

    //Alert dialog with to choices: Open camera or Choice from library
    private fun showAlertDialog(){
        val builder = AlertDialog.Builder(this.context)
        builder.setTitle("Upload profile picture")
        builder.setSingleChoiceItems(
            alertItems,
            -1
        ) { _, which ->
            if(which == 0){
              openCamera()
            } else {
             openGallery()
            }
        }
        builder.setPositiveButton(
            "OK"
        ) { _, _ ->

        }
         val dialog = builder.create()
        // Display the alert dialog on interface
        dialog.show()
    }

}


