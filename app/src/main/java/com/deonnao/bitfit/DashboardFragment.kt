package com.deonnao.bitfit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment



class DashboardFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        parentFragmentManager.setFragmentResultListener("log", this) { requestKey, bundle ->
            var result = Integer.parseInt(bundle.getString(TAG)) //90
//            var totalCalories = LogFragment().totalCalories
//            var numOfFoods = LogFragment().numOfFoods
            getMinMax(view, result)
        }
        return view
    }

    companion object {
        fun newInstance(param1: String, param2: String) =
            LogFragment().apply {
            }
    }
    var min = 0
    var max = 0
    var average = 0
    fun getMinMax(view: View, result: Int) {

//        var averageCals = view.findViewById<TextView>(R.id.averageCalsTv)
        var minCals = view.findViewById<TextView>(R.id.minCalsTv)
        var maxCals = view.findViewById<TextView>(R.id.maxCalsTv)

        if ((min == 0) or (min > result)){
            min = result
        }

        if ((max == 0) or (max < result)){
            max = result
        }

//        average = totalCalories/numOfFoods

//        var max = Integer.parseInt(maxCals.text.toString())
        var average = result

        if(min > max) {
            var temp = max
            max = min
            min = temp
        }
        minCals.text = min.toString()
        maxCals.text = max.toString()
//        averageCals.text = average.toString()
    }
}