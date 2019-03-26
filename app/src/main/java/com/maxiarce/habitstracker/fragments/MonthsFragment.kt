package com.maxiarce.habitstracker.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.maxiarce.habitstracker.helpers.DatabaseHelper
import com.maxiarce.habitstracker.R
import com.maxiarce.habitstracker.adapters.HabitsMonthAdapter


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class MonthsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val db = DatabaseHelper(activity!!.applicationContext)
        var data = db.readData()

        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_months, container, false)


        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView_month)
        val linearLayoutManager = LinearLayoutManager(view.context,LinearLayoutManager.HORIZONTAL,false)
        val snapHelper = PagerSnapHelper()

        snapHelper.attachToRecyclerView(recyclerView)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = HabitsMonthAdapter(data)


        return view
    }


}
