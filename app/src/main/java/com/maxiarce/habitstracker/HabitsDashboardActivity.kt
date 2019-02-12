package com.maxiarce.habitstracker

import android.animation.Animator
import android.app.Activity
import android.content.res.ColorStateList
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.content.res.ResourcesCompat
import android.util.Log
import android.view.View
import android.view.ViewAnimationUtils
import kotlinx.android.synthetic.main.activity_habits_dashboard.*


class HabitsDashboardActivity : AppCompatActivity() {
    var floatinButtonStatus = false;
    var habitsList = mutableListOf<String>()
    init {
        habitsList.add("Chau")
        habitsList.add("Chau")
        habitsList.add("Chau")
        habitsList.add("Chau")
        habitsList.add("Chau")
        habitsList.add("Chau")
        habitsList.add("Chau")
        habitsList.add("Chau")
        habitsList.add("Chau")
        habitsList.add("Chau")
        habitsList.add("Chau")
        habitsList.add("Chau")
        habitsList.add("Chau")
        habitsList.add("Chau")
        habitsList.add("Chau")
        habitsList.add("Chau")
        habitsList.add("Chau")
        habitsList.add("Chau")
        habitsList.add("Chau")
    }

    //listener for bottomNavBar
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {item->
        when(item.itemId){
            R.id.habits_month_view ->{
                Log.d("NavItemSelectedListener","Month Selected")
                return@OnNavigationItemSelectedListener true
            }
            R.id.habits_list_view ->{
                Log.d("NavItemSelectedListener","List Selected")
                replaceFragment(HabitsListFragment())
                return@OnNavigationItemSelectedListener true

            }
            R.id.habits_goals ->{
                Log.d("NavItemSelectedListener","Goals Selected")
                return@OnNavigationItemSelectedListener true

            }
        }
        false

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_habits_dashboard)

        //replace fragment
        replaceFragment(HabitsListFragment())

        //set the bottomNavBar
        bottomNavigationView.selectedItemId = R.id.habits_list_view
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)


        //set floatbutton
        floatingActionButton_new_habit.setOnClickListener {
            floatingActionButton_new_habit.hide()
            revealFragmentAddHabit()
        }


    }

    private fun replaceFragment (fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.fragment_container,fragment)
        fragmentTransaction.commit()
        fragment.fragmentManager!!.executePendingTransactions()
    }

    //circular animation reveal/hides add habit fragment
    fun revealFragmentAddHabit(){
        val locations = IntArray(2)

        if(!floatinButtonStatus){
            bottomNavigationView.visibility = View.GONE

            floatingActionButton_new_habit.getLocationOnScreen(locations)

            val x = locations[0] + (floatingActionButton_new_habit.width/2)
            val y = locations[1] + (floatingActionButton_new_habit.height/2)
            Log.d("POS POS","$x and $y")


            val startRadius = 0
            val endingRadius = Math.hypot(fragment_container.width.toDouble(),fragment_container.height.toDouble())

            val  anim = ViewAnimationUtils.createCircularReveal(fragment_container,x,y,startRadius.toFloat(),endingRadius.toFloat())
            anim.duration = 250
            replaceFragment(AddHabitFragment())
            anim.addListener(object : Animator.AnimatorListener{
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    floatingActionButton_new_habit.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#64dd17"))
                    floatingActionButton_new_habit.setImageResource(R.drawable.ic_done)
                    floatingActionButton_new_habit.show()
                    floatinButtonStatus = true

                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                }

            })
            anim.start()



            floatinButtonStatus = true

        }else{

                floatingActionButton_new_habit.getLocationOnScreen(locations)

                val x = locations[0] + (floatingActionButton_new_habit.width/2)
                val y = locations[1] + (floatingActionButton_new_habit.height/2)


                val startRadius = Math.max(fragment_container.width.toDouble(),fragment_container.height.toDouble())
                val endingRadius = 0


                val  anim = ViewAnimationUtils.createCircularReveal(fragment_container,x,y,startRadius.toFloat(),endingRadius.toFloat())
                anim.duration = 250

                anim.addListener(object : Animator.AnimatorListener{
                    override fun onAnimationRepeat(animation: Animator?) {
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        replaceFragment(HabitsListFragment())
                        floatingActionButton_new_habit.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#00ACC1"))
                        floatingActionButton_new_habit.setImageResource(R.drawable.ic_add)
                        bottomNavigationView.visibility = View.VISIBLE
                        floatingActionButton_new_habit.show()
                        floatinButtonStatus = false
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                    }

                    override fun onAnimationStart(animation: Animator?) {
                    }

                })
                anim.start()

        }


    }



}




