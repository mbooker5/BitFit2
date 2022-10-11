package com.deonnao.bitfit

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


const val TAG = "ITEM"
const val TAG2 = "CALORIES"
class LogFragment : Fragment() {

    public var listOfItems = mutableListOf<Nutrition>()
    public val numOfFoods = listOfItems.size
    public val totalCalories = 0
    lateinit var adapter: NutritionAdapter
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_log, container, false)

        //val intent = Intent(context, LogFragment::class.java)
        //val items = intent.getStringExtra(TAG)
        //val calories = intent.getStringExtra(TAG2)
        //Log.i("dashboard", items.toString())
        val onLongClickListener = object : NutritionAdapter.onLongClickListener {
            override fun onItemLongClicked(position: Int) {
                //Remove the item from the list
                listOfItems.removeAt(position)
                //Notify the adapter that our data set has changed
                adapter.notifyDataSetChanged()
            }
        }

        val layoutManager = LinearLayoutManager(context)
        recyclerView = view.findViewById(R.id.rvList)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        adapter = NutritionAdapter(listOfItems, onLongClickListener)
        recyclerView.adapter = adapter

        //values to reference the views from the layout file
        val foodItem = view.findViewById<EditText>(R.id.etFoodItem)
        val etCalories = view.findViewById<EditText>(R.id.etCalories)
        val submitBtn = view.findViewById<Button>(R.id.submitBtn)


        submitBtn.setOnClickListener {
            //Grab the text that the user inputs
            val item = foodItem.text.toString()
            val calories = etCalories.text.toString()
            val allItems = Nutrition(item, calories)
            listOfItems.add(allItems)
            adapter.notifyItemInserted(listOfItems.size - 1)
//            totalCalories += Integer.parseInt(calories)
            //val intent = Intent(context, DashboardFragment::class.java)
            //intent.putExtra(TAG, calories)
            //Log.i(TAG, calories)


            val bundle = Bundle()
            bundle.putString(TAG, calories)
            //Log.i("cals", calories)
            parentFragmentManager.setFragmentResult("log", bundle)


            //Reset text field after user adds item to the wishlist
            foodItem.setText("")
            etCalories.setText("")
            foodItem.hideKeyboard()
        }

        return view
    }

    companion object {
        fun newInstance(param1: String, param2: String) =
            LogFragment().apply {
            }
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}