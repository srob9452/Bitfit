package com.codepath.bitfit_part1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.bitfit_part1.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class ListActivity : AppCompatActivity() {
    private val foods = mutableListOf<DisplayFood>()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("ListActivity", "launching")

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        // handle navigation selection
        bottomNavigationView.setOnItemSelectedListener { item ->
            lateinit var fragment: Fragment
            when (item.itemId) {
                R.id.logTab -> fragment = LogFragment()
                R.id.dashboardTab -> fragment = DashboardFragment()
            }
            replaceFragment(fragment)
            handleNewEntry()
            true
        }

        // Set default selection
        bottomNavigationView.selectedItemId = R.id.dashboardTab
    }

    private fun replaceFragment(newFragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.article_frame_layout, newFragment)
        fragmentTransaction.commit()
    }

    private fun handleNewEntry() {
        // Need to check if this ListActivity has an extra
        // If so -- that's a new food to add passed from EntryActivity!
        val food = intent.getSerializableExtra("ENTRY_EXTRA")
        // First, check if the EXTRA exists:
        if (food != null) {
            Log.d("ListActivity", "got an extra")
            Log.d("ListActivity", (food as DisplayFood).toString())
            // Since there's an extra, let's add it to the DB.
            lifecycleScope.launch(IO) {
                (application as BitFitApplication).db.foodDao().insert(
                    FoodEntity(
                        name = food.name,
                        calories = food.calories
                    )
                )
            }
            intent.removeExtra("ENTRY_EXTRA")
        }
        else {
            // No extra, so we don't need to do anything.
            Log.d("ListActivity", "no extra")
        }

        // If the "Add Food" button is clicked, swap to EntryActivity
        var addFoodButtonView : Button = findViewById(R.id.button)
        addFoodButtonView.setOnClickListener {
            Log.d("ListActivity", "add new food clicked")
            val intent = Intent(this, EntryActivity::class.java)
            this.startActivity(intent)
        }
    }
}