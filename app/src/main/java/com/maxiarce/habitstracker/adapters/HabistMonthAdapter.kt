           package com.maxiarce.habitstracker.adapters

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Color.TRANSPARENT
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.maxiarce.habitstracker.models.HabitItem
import com.maxiarce.habitstracker.R
import com.sdsmdg.harjot.vectormaster.VectorMasterDrawable
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
        val daysleft = data[position].type - data[position].days
        val daysToReveal = convertStringtoArray(data[position].randomDaysReveal)
        var vectorMasterDrawable = VectorMasterDrawable(context,R.drawable.mask_month_image)


        if(data[position].type == 60){
             vectorMasterDrawable = VectorMasterDrawable(context,R.drawable.mask_month_image_60)
        }

        card.textView_calendar_title.text = data[position].habitText
        card.textView_calendar_description.text = data[position].habitDescription
        card.ContraintLayout_habits_calendar.setBackgroundColor(Color.parseColor(data[position].color))
        card.imageView_background_calendar.setImageBitmap(BitmapFactory.decodeFile("/data/data/com.maxiarce.habitstracker/files/$title"))
        card.vector_imageMask.setImageDrawable(vectorMasterDrawable)
        card.textView_days_left.text = "$daysleft days left before getting your picture back"

        val maskBackground =vectorMasterDrawable.getPathModelByName("background")
        maskBackground.fillColor = Color.parseColor(data[position].color)


        //paint the mask
        for (j in 1..data[position].days) {
            val dotToReveal = daysToReveal[j]
            val dot = vectorMasterDrawable.getPathModelByName("dot$dotToReveal")
            dot.fillColor = TRANSPARENT
        }

        for(i in 1..data[position].type){
            val dot = vectorMasterDrawable.getPathModelByName("dot$i")
            dot.strokeColor = Color.parseColor(data[position].color)
        }

        //set the mask
        card.vector_imageMask.setImageDrawable(vectorMasterDrawable)


    }

    fun convertStringtoArray (str: String):List<String>{
        val strSeparator = ","
        return str.split(strSeparator)
    }



}

class ViewHolderCalendar (view: View) : RecyclerView.ViewHolder(view) {

    var card = view
}