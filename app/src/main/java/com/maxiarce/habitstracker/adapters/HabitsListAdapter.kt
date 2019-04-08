package com.maxiarce.habitstracker.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.maxiarce.habitstracker.*
import com.maxiarce.habitstracker.helpers.AnimateHabitClickListener
import com.maxiarce.habitstracker.helpers.DatabaseHelper
import com.maxiarce.habitstracker.models.HabitItem
import kotlinx.android.synthetic.main.row_habits.view.*



class HabitsAdapter(val animateHabitClickListener: AnimateHabitClickListener, private var data : MutableList<HabitItem>): RecyclerView.Adapter<ViewHolder>() {
    lateinit var context: Context
    lateinit var db: DatabaseHelper

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        context = viewGroup.context
        db = DatabaseHelper(context)
        return ViewHolder(
            LayoutInflater.from(viewGroup.context).inflate(
                R.layout.row_habits,
                viewGroup,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        if(data[position].days < data[position].type){
            var row = viewHolder.row
            val typeface = ResourcesCompat.getFont(row.context, R.font.googlesans)
            row.habits_row_text.text = data[position].habitText
            row.habits_row_text.typeface = typeface
            row.habits_row_description.text = data[position].habitDescription

            val background = row.imageView_dot_background
            val shape : GradientDrawable = background.background as GradientDrawable
            shape.setColor(Color.parseColor(data[position].color))

            viewHolder.row.radioButton_habit.setOnClickListener {
                if(viewHolder.row.radioButton_habit.isChecked){
                    updateStatus(position)
                }
            }

            viewHolder.itemView.setOnClickListener{
                animateHabitClickListener.onHabitClick(data[position].id, row.habits_row_text,row.habits_row_description,row.imageView_dot_background,data[position].color)
            }

            if (data[position].status ==1){
                row.radioButton_habit.isChecked = true
            }
        }

    }


    fun removeRowFromRecyclerView(position: Int){
        val deleteItem = data[position].id
        data.removeAt(position)
        db.deleteData(deleteItem)
        notifyItemRemoved(position)
    }

    fun updateStatus(position: Int){
        db.updateDataDays(data[position])
    }

}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    var row = view
}