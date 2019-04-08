package com.maxiarce.habitstracker.fragments


import android.graphics.Color
import com.maxiarce.habitstracker.R
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.res.ResourcesCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.maxiarce.habitstracker.helpers.DatabaseHelper
import kotlinx.android.synthetic.main.fragment_goals.*
import java.util.concurrent.TimeUnit


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class GoalsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_goals, container, false)

        val db = DatabaseHelper(context!!)
        val habitsData = db.readData()
        var daysInRowTotal = 0
        var daysOutOfRow = 0

        for(i in 0 until habitsData.size){
            daysInRowTotal += (habitsData[i].daysInRowTotal)
            daysOutOfRow += (habitsData[i].daysOutOfRow)
        }



        val pieChart = view.findViewById<PieChart>(R.id.pieChart_fragment_goals)
        pieChart.setUsePercentValues(true)
        pieChart.setEntryLabelTextSize(28f)
        pieChart.setEntryLabelColor(Color.BLACK)
        val typeface = ResourcesCompat.getFont(context!!, R.font.googlesans)
        pieChart.setEntryLabelTypeface(typeface)


        val data = ArrayList<PieEntry>()
        data.add(PieEntry(daysInRowTotal.toFloat(),"Made it"))
        data.add(PieEntry(daysOutOfRow.toFloat(),"Fail"))

        val dataSet =  PieDataSet(data,"names")

        dataSet.valueTextSize = 24f
        dataSet.valueTextColor = R.color.colorWhite


        val colors = ArrayList<Int>()
        colors.add(Color.rgb(39,178,228))
        colors.add(Color.rgb(230,188,203))

        dataSet.colors = colors

        val dataPie = PieData(dataSet)

        pieChart.data = dataPie

        if (daysInRowTotal == 0 &&  daysOutOfRow == 0){
            pieChart.visibility = View.GONE
        }


        return view
    }


}
