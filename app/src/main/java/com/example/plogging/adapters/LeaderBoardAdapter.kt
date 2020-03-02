package com.example.plogging.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.plogging.R
import com.example.plogging.data.model.ClassTrash
import kotlinx.android.synthetic.main.recycler_view_item.view.*

class LeaderBoardAdapter (private val myDataset: List<ClassTrash>) :
    RecyclerView.Adapter<LeaderBoardAdapter.MyViewHolder>() {


        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder.
        // Each data item is just a string in this case that is shown in a TextView.
        class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)


        // Create new views (invoked by the layout manager)
        override fun onCreateViewHolder(parent: ViewGroup,
                                        viewType: Int): MyViewHolder {

            // create a new view
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_view_item, parent, false)
            // set the view's size, margins, paddings and layout parameters
            return MyViewHolder(view)
        }

        // Replace the contents of a view (invoked by the layout manager)
        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            // - get element from your data set at this position
            // - replace the contents of the view with that element

            val meal = myDataset[position]
           // holder.itemView.value_number.text = meal.meal!!.mealName
            //holder.itemView.value_username.text = meal.type?.type
            //holder.itemView.value_points.text = meal.meal!!.mealDescription
        }

        // Return the size of your data set (invoked by the layout manager)
        override fun getItemCount() = myDataset.size

}