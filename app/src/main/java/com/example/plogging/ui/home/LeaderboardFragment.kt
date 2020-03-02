package com.example.plogging.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plogging.R
import com.example.plogging.adapters.LeaderBoardAdapter
import com.example.plogging.data.model.ClassTrash
import com.example.plogging.data.model.ClassUserTrash
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_leaderboard.*
import java.lang.Integer.parseInt
import java.util.*
import kotlin.collections.HashMap

class LeaderBoardFragment: Fragment(){

    var mFirebaseDB =  FirebaseDatabase.getInstance().reference
    var listOfTrash1: MutableList<ClassUserTrash> = java.util.ArrayList()
    lateinit var child: MutableIterable<DataSnapshot>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_leaderboard, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_leaderboard)

        mFirebaseDB.child("users")
            .addValueEventListener(object: ValueEventListener {
                @SuppressLint("SetTextI18n")
                override fun onDataChange(p0: DataSnapshot) {
                    val children = p0.children
                    var total = 0
                    var username = ""

                    children.forEach {
                        Log.d("p0.value", it.value.toString())
                        if (it.child("trash").value != null) {
                            username = it.child("username").value.toString()
                            val child = it.child("trash")
                            child.children.forEach{
                                total += parseInt(it.child("total").value.toString())
                                Log.d("p0", it.child("total").value.toString())
                            }
                            Log.d("Total", total.toString())
                            listOfTrash1.add(ClassUserTrash(username, total))
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
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

}