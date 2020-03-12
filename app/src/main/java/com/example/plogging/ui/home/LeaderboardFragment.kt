package com.example.plogging.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.plogging.R
import com.example.plogging.utils.getUsersAndTotalPointsFromDB

class LeaderBoardFragment: Fragment(){

    //FUNCTIONS AND INTERFACES:
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_leaderboard, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_leaderboard)
        getUsersAndTotalPointsFromDB(recyclerView,context!!)
        return view
    }
}