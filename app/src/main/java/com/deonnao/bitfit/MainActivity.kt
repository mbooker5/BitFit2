package com.deonnao.bitfit


import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.autofill.UserData
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
//import com.codepath.bitfit.databinding.ActivityMainBinding
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deonnao.bitfit.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json


fun createJson() = Json {
    isLenient = true
    ignoreUnknownKeys = true
    useAlternativeNames = false
}

class MainActivity : AppCompatActivity() {
    var listOfItems = mutableListOf<Nutrition>()
    lateinit var adapter: NutritionAdapter
    //private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //binding = ActivityMainBinding.inflate(layoutInflater)
        //val view = binding.root
        //setContentView(view)

        //values to reference the views from the layout file
        val foodItem = findViewById<EditText>(R.id.etFoodItem)
        val etCalories = findViewById<EditText>(R.id.etCalories)
        val submitBtn = findViewById<Button>(R.id.submitBtn)

        val onLongClickListener = object : NutritionAdapter.onLongClickListener {
            override fun onItemLongClicked(position: Int) {
                //Remove the item from the list
                listOfItems.removeAt(position)
                //Notify the adapter that our data set has changed
                adapter.notifyDataSetChanged()
            }
        }
        // Lookup the recyclerview in activity layout
        val recyclerView = findViewById<RecyclerView>(R.id.rvList)
        // Create adapter passing in the listOfItems and the long click listener
        adapter = NutritionAdapter(listOfItems, onLongClickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)

        submitBtn.setOnClickListener {
            //Grab the text that the user inputs
            val item = foodItem.text.toString()
            val calories = etCalories.text.toString()
            //Reset text field after user adds item to the wishlist
            foodItem.setText("")
            etCalories.setText("")
            val wishItems = Nutrition(item, calories)
            listOfItems.add(wishItems)
            adapter.notifyItemInserted(listOfItems.size - 1)
            foodItem.hideKeyboard()
        }

        lifecycleScope.launch {
            (application as NutritionApplication).db.NutritionDao().getAll()
                .collect { databaseList ->
                    databaseList.map { entity ->
                        Nutrition(
                            entity.food.toString(),
                            entity.calories.toString()
                        )
                    }.also { mappedList ->
                        listOfItems.clear()
                        listOfItems.addAll(mappedList)
                        adapter.notifyDataSetChanged()
                    }
                }
        }

        /**
         * This is the place with the error. I am trying to insert data into the database and
         * not sure how to do it
         */
        /*let { list ->
            lifecycleScope.launch(Dispatchers.IO) {
                (application as NutritionApplication).db.NutritionDao().deleteAll()
                (application as NutritionApplication).db.NutritionDao().insertAll(list.map {
                    NutritionEntity(
                        food = food,
                        calories = calories
                    )
                })
            }
        }*/
    }

    //function to hide the keyboard after the user enters their desired item to the wishlist
    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

}