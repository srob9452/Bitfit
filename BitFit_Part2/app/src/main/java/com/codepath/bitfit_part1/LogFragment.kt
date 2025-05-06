package com.codepath.bitfit_part1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.bitfit_part1.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class LogFragment() : Fragment() {
    private val foods = mutableListOf<DisplayFood>()
    private lateinit var foodRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_log, container, false)

        foodRecyclerView = view.findViewById(R.id.food_recycler_view)
        val bitfitAdapter = BitFitAdapter(view.context, foods)
        foodRecyclerView.adapter = bitfitAdapter
        foodRecyclerView.layoutManager = LinearLayoutManager(view.context).also {
            val dividerItemDecoration = DividerItemDecoration(view.context, it.orientation)
            foodRecyclerView.addItemDecoration(dividerItemDecoration)
        }

        // Using Flow, update the RecyclerView whenever the db is updated
        // Taken from the provided Lab 5 code
        lifecycleScope.launch {
            (activity?.application as BitFitApplication).db.foodDao().getAll().collect { databaseList ->
                databaseList.map { entity ->
                    DisplayFood(
                        entity.name,
                        entity.calories
                    )
                }.also { mappedList ->
                    foods.clear()
                    foods.addAll(mappedList)
                    bitfitAdapter.notifyDataSetChanged()
                }
            }
        }

        return view
    }

    fun getAdapter(): BitFitAdapter {
        return foodRecyclerView.adapter as BitFitAdapter
    }

    fun getFoods(): MutableList<DisplayFood> {
        return foods
    }

    companion object {
        fun newInstance(): LogFragment {
            return LogFragment()
        }
    }
}