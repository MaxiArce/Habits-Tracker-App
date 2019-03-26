package com.maxiarce.habitstracker.helpers

import android.widget.ImageView
import android.widget.TextView

interface AnimateHabitClickListener {

    fun onHabitClick(id: Int, textTitle: TextView,textDescription: TextView, dot :ImageView,dotColor:String){}
}