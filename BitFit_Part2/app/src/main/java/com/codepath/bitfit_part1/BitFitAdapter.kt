package com.codepath.bitfit_part1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BitFitAdapter(private val context: Context, private val foods: List<DisplayFood>) :
    RecyclerView.Adapter<BitFitAdapter.ViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_food, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = foods[position]
        holder.bind(article)
    }

    override fun getItemCount() = foods.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        private val foodTextView = itemView.findViewById<TextView>(R.id.foodName)
        private val caloriesTextView = itemView.findViewById<TextView>(R.id.foodCalories)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(food: DisplayFood) {
            foodTextView.text = food.name
            caloriesTextView.text = food.calories.toString()
        }

        override fun onClick(v: View?) {
            // If you wanted to add a details screen/edit for clicking
            // on a particular entry, you could do it here.
        }
    }
}