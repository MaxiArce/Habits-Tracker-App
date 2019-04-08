package com.maxiarce.habitstracker.activities


import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.RadioGroup
import com.maxiarce.habitstracker.helpers.DatabaseHelper
import com.maxiarce.habitstracker.R
import com.maxiarce.habitstracker.models.HabitItem
import kotlinx.android.synthetic.main.activity_edit_habit.*

class EditHabitActivity : AppCompatActivity() {

    val db = DatabaseHelper(this)
    lateinit var item : HabitItem


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_habit)


        val extras = intent.extras
        val id = extras.getInt("id")
        var edit = false
        loadHabit(id)

        var colorPick : String
        val fb = findViewById<FloatingActionButton>(R.id.floatingActionButton_edit)
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup_pick_color_edit_activity)

        fb.setImageDrawable(getDrawable(R.drawable.ic_edit))




        val background = imageView_dot_background
        val shape : GradientDrawable = background.background as GradientDrawable
        shape.setColor(Color.parseColor(item.color))
        shape.cornerRadius = 0f

        editText_habit_title_edit_activity.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                item.habitText = editText_habit_title_edit_activity.text.toString()
            }

        })

        editText_habit_description_edit_activity.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                item.habitDescription = editText_habit_description_edit_activity.text.toString()
            }

        })

        floatingActionButton_edit.setOnClickListener {
            if (!edit){
                edit = true
                viewSwitcher_habit_title.showNext()
                editText_habit_title_edit_activity.setText(item.habitText)
                editText_habit_description_edit_activity.setText(item.habitDescription)
                fb.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#7CB319"))
                fb.setImageDrawable(getDrawable(R.drawable.ic_done))
                if (item.type == 30){
                    button_change_type_edit_activity.visibility = View.VISIBLE
                }

            }else{
                if(edit){
                    edit = false
                    viewSwitcher_habit_title.showNext()
                    floatingActionButton_edit.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
                    fb.setImageDrawable(getDrawable(R.drawable.ic_edit))
                    saveEditHabit(item)
                    loadHabit(id)

                }
            }



        }

        radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            when(i){
                radio1_edit_activity.id -> colorPick = "#e57373"
                radio2_edit_activity.id -> colorPick = "#9575CD"
                radio3_edit_activity.id -> colorPick = "#4FC3F7"
                radio4_edit_activity.id -> colorPick = "#81C784"
                radio5_edit_activity.id -> colorPick = "#DCE775"
                radio6_edit_activity.id -> colorPick = "#FFF176"
                radio7_edit_activity.id -> colorPick = "#FFB74D"
                radio8_edit_activity.id -> colorPick = "#A1887F"
                else -> colorPick = ""
            }
            shape.setColor(Color.parseColor(colorPick))
            item.color = colorPick
        }

        button_change_type_edit_activity.setOnClickListener{
            item.type = 60
            button_change_type_edit_activity.isClickable = false
            button_change_type_edit_activity.setText("Now this is a 60 days calendar")
        }


    }

    fun saveEditHabit(item: HabitItem){
        val db = DatabaseHelper(this)
        db.updateById(item)
    }

    fun loadHabit(id: Int){
        item = db.searchById(id)
        val daysLeft = item.type - item.days

        textView_habit_title_edit_activity.text = item.habitText
        if (item.habitDescription.isNotBlank()){
            textView_habit_description_edit_activity.text = item.habitDescription
        }
        else{
            textView_habit_description_edit_activity.text = "No Description"
        }

        textView_days_in_a_row_edit_activity.text = "Currently ${item.daysInRow} days in a row"
        textView_days_left_edit_activity.text = "$daysLeft days left before getting your picture back"
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val background = imageView_dot_background
        val shape : GradientDrawable = background.background as GradientDrawable
        shape.cornerRadius = 25f


    }

}
