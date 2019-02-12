package com.maxiarce.habitstracker

import android.graphics.Typeface
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.habits_row.view.*

class HabitsAdapter(private var habits : MutableList<String>): RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.habits_row, viewGroup, false))
    }

    override fun getItemCount(): Int {
        return habits.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        var row = viewHolder.row
        val typeface = ResourcesCompat.getFont(row.context,R.font.googlesans)
        row.habits_row_text.text = habits[position]
        row.habits_row_text.typeface = typeface

        viewHolder.row.checkBox.setOnClickListener {
            if(viewHolder.row.checkBox.isChecked)
                removeRow(viewHolder.adapterPosition)
                viewHolder.row.checkBox.isChecked = false
        }
    }


    fun removeRow(position: Int){
        habits.removeAt(position)
        notifyItemRemoved(position)
    }

}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    var row = view
}