package com.codepath.bitfit_part1

import android.content.Intent
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.update
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.lang.Long.MAX_VALUE

class DashboardFragment : Fragment() {
    private lateinit var averageView: TextView
    private lateinit var maxView: TextView
    private lateinit var minView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        averageView = view.findViewById(R.id.avgTextView)
        minView = view.findViewById(R.id.minTextView)
        maxView = view.findViewById(R.id.maxTextView)

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
                    update(mappedList)
                }
            }
        }

        var clearButtonView : Button = view.findViewById(R.id.clearButton)
        clearButtonView.setOnClickListener {
            lifecycleScope.launch(IO) {
                (activity?.application as BitFitApplication).db.foodDao().deleteAll()
            }
        }

        return view
    }

    private fun update(foods: List<DisplayFood>) {

        if (foods.isEmpty()) {
            averageView.text = "No Data"
            minView.text = "No Data"
            maxView.text = "No Data"
            return
        }

        var min : Long = Long.MAX_VALUE
        var max : Long = Long.MIN_VALUE
        var sum : Long = 0
        for (food in foods) {
            food.calories?.let {
                sum += it
                if (it < min) min = it
                if (it > max) max = it
            }
        }

        //Update the average, max, and min
        averageView.text = (sum / foods.size).toString()
        minView.text = min.toString()
        maxView.text = max.toString()
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DashboardFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(): DashboardFragment {
            return DashboardFragment()
        }
    }
}