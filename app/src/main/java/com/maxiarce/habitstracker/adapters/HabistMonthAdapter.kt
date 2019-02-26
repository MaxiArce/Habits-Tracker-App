package com.maxiarce.habitstracker.adapters

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.maxiarce.habitstracker.models.HabitItem
import com.maxiarce.habitstracker.R
import com.sdsmdg.harjot.vectormaster.VectorMasterView
import kotlinx.android.synthetic.main.row_habits_calendar.view.*


class HabitsMonthAdapter(private var data : MutableList<HabitItem>): RecyclerView.Adapter<ViewHolderCalendar>() {

    lateinit var context: Context

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolderCalendar {
        context = viewGroup.context

        return ViewHolderCalendar(LayoutInflater.from(viewGroup.context).inflate(R.layout.row_habits_calendar, viewGroup, false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolderCalendar, position: Int) {
        val card = viewHolder.card
        val title = data[position].habitText
        card.textView_calendar_title.text = data[position].habitText
        card.textView_calendar_description.text = data[position].habitDescription
        card.imageView_background_calendar.setImageBitmap(BitmapFactory.decodeFile("/data/data/com.maxiarce.habitstracker/files/$title"))
        val maskVector = card.findViewById<VectorMasterView>(R.id.vector_imageMask)

        for (j in 1..data[position].days) {
            val dot = maskVector.getPathModelByName("dot$j")
            dot.fillColor = Color.TRANSPARENT
        }
        for(i in 1..30){
            val dot = maskVector.getPathModelByName("dot$i")
            dot.strokeColor = Color.parseColor(data[position].color)
        }

        var daysleft = 30-data[position].days
        card.textView_days_left.text = "$daysleft days left before getting your picture back"

    }


}

class ViewHolderCalendar (view: View) : RecyclerView.ViewHolder(view) {

    var card = view
}