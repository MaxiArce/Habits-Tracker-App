package com.maxiarce.habitstracker.activities

import android.animation.Animator
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.util.Pair
import android.support.design.widget.AppBarLayout
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.maxiarce.habitstracker.helpers.AnimateHabitClickListener
import com.maxiarce.habitstracker.helpers.DatabaseHelper
import com.maxiarce.habitstracker.R
import com.maxiarce.habitstracker.adapters.HabitsAdapter
import com.maxiarce.habitstracker.fragments.MonthsFragment
import com.maxiarce.habitstracker.models.HabitItem
import kotlinx.android.synthetic.main.activity_habits_dashboard.*
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class HabitsDashboardActivity : AppCompatActivity(), AnimateHabitClickListener {

    lateinit var appbar: AppBarLayout
    lateinit var db: DatabaseHelper
    lateinit var data: MutableList<HabitItem>
    lateinit var recyclerView: RecyclerView


    //listener for bottomNavBar
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.habits_month_view -> {
                appbar.visibility = View.GONE
                floatingActionButton_new_habit.hide()
                replaceFragment(MonthsFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.habits_list_view -> {
                Log.d("NavItemSelectedListener", "List Selected")
                closeFragment()
                return@OnNavigationItemSelectedListener true

            }
            R.id.habits_goals -> {
                Log.d("NavItemSelectedListener", "Goals Selected")
                return@OnNavigationItemSelectedListener true

            }
        }
        false

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_habits_dashboard)

        intent = Intent(this, AddHabitActivity::class.java)

        appbar = appbar_layout

        //read sqlite
        db = DatabaseHelper(this)
        data = db.readData()

        updateHabits()

        //set the recycler View
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView_habits_dashboard)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = HabitsAdapter(this,data)

        // Set AppBar
        val collapsingToolBar = collapsingToolBar_layout
        //change font
        val typeface = ResourcesCompat.getFont(this, R.font.googlesansregular)
        collapsingToolBar.setCollapsedTitleTypeface(typeface)
        collapsingToolBar.setExpandedTitleTypeface(typeface)
        //set date
        textView_date.text = "${Calendar.getInstance().get(Calendar.MONTH)+1}/${Calendar.getInstance().get(Calendar.DAY_OF_MONTH)}"

        //set itemTouchHelper for swipe to complete row
        val habitsRowSwipeCallback = HabitsRowSwipe(recyclerView.context)
        val itemTouchHelper = ItemTouchHelper(habitsRowSwipeCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        //set the bottomNavBar
        bottomNavigationView.selectedItemId = R.id.habits_list_view
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        bottomNavigationView.visibility = View.VISIBLE

        //set floatbutton
        floatingActionButton_new_habit.setOnClickListener {
            revealAdHabitActivity()
        }


    }

    override fun onResume() {
        super.onResume()
        db = DatabaseHelper(this)
        data = db.readData()
        recyclerView.adapter = HabitsAdapter(this,data)

    }
    fun updateHabits(){
        //update status of the habits
        for(i in 0 until data.size){
            val diffDays = System.currentTimeMillis() - data[i].dateStamp
            Log.d("DAYSDIFF",(TimeUnit.MILLISECONDS.toDays(diffDays).toString()) )
            if(TimeUnit.MILLISECONDS.toDays(diffDays)>0 && data[i].status == 1) {
                data[i].status = 0
                db.updateDataStatus(data[i].id, 0)
            }
        }
    }

    fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
        fragment.fragmentManager!!.executePendingTransactions()
    }


    fun closeFragment( ) {
        appbar.visibility =  View.VISIBLE
        floatingActionButton_new_habit.show()
        supportFragmentManager.popBackStack()

    }


    //circular animation reveal add activity
    fun revealAdHabitActivity(){

        val locations = IntArray(2)
        floatingActionButton_new_habit.getLocationOnScreen(locations)

        val x = locations[0] + (floatingActionButton_new_habit.width / 2)
        val y = locations[1] + (floatingActionButton_new_habit.height / 2)

        this.window.decorView.height

        val startRadius = 0
        val endingRadius = Math.hypot(this.window.decorView.width.toDouble(), this.window.decorView.height.toDouble())

        val anim = ViewAnimationUtils.createCircularReveal(
            coordinatorLayout_main,
            x,
            y,
            startRadius.toFloat(),
            endingRadius.toFloat()
        )

        anim.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
                startActivity(intent)
            }

        })
        anim.start()

    }


    fun deletionAlert(adapter: HabitsAdapter, viewHolder: RecyclerView.ViewHolder){
        val deletionAlert = AlertDialog.Builder(this)
        deletionAlert.setTitle("Confirm")
        deletionAlert.setMessage("Are you sure you want to delete this?")
        deletionAlert.setPositiveButton("YES") { dialog, _ ->
            adapter.removeRowFromRecyclerView(viewHolder.adapterPosition)
            dialog.dismiss()
        }

        deletionAlert.setNegativeButton("NO", DialogInterface.OnClickListener { dialog, _ ->
            adapter.notifyItemChanged(viewHolder.adapterPosition)
            dialog.dismiss()
        })
        deletionAlert.setCancelable(false)
        val alert = deletionAlert.create()
        alert.show()


    }


    override fun onHabitClick(id: Int, textTitle: TextView,textDescription: TextView, dot: ImageView,dotColor: String) {
        var intent =  Intent(this, EditHabitActivity::class.java)


        intent.putExtra("id",id)

        val pairs = ArrayList<Pair<View, String>>(2)
        pairs.add(Pair.create(textTitle,"titleTransition"))
        pairs.add(Pair.create(textDescription,"descriptionTransition"))
        pairs.add(Pair.create(dot,"dotTransition"))
        val pairsArray = pairs.toTypedArray()

        var options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this,*pairsArray)

        startActivity(intent, options.toBundle());
    }


    inner class HabitsRowSwipe(context: Context) : ItemTouchHelper.Callback() {

        private val doneIcon = ContextCompat.getDrawable(context, R.drawable.ic_done)
        private val doneIntrinsicWidth = doneIcon!!.intrinsicWidth
        private val doneIntrinsicHeight = doneIcon!!.intrinsicHeight

        private val deleteIcon = ContextCompat.getDrawable(context, R.drawable.ic_delete)
        private val deleteIntrinsicWidth = deleteIcon!!.intrinsicWidth
        private val deleteIntrinsicHeight = deleteIcon!!.intrinsicHeight


        private val clearPaint = Paint()


        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
            val completeFlag = ItemTouchHelper.LEFT
            return makeMovementFlags(0, completeFlag)

        }

        override fun onMove(
            recyclerView: RecyclerView,
            originViewHolder: RecyclerView.ViewHolder,
            targetVIewHolder: RecyclerView.ViewHolder
        ): Boolean {
            return false

        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            if (direction == ItemTouchHelper.LEFT) {
                val adapter = recyclerView_habits_dashboard.adapter as HabitsAdapter
                deletionAlert(adapter,viewHolder)

            }

        }

        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {

            val itemView = viewHolder.itemView
            val itemHeight = itemView.bottom - itemView.top
            val isCanceled = dX == 0f && !isCurrentlyActive

            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                //right swipe - done habit
                if (dX > 0) {

                    if (isCanceled) {
                        clearCanvas(
                            c,
                            itemView.left + dX,
                            itemView.top.toFloat(),
                            itemView.left.toFloat(),
                            itemView.bottom.toFloat()
                        )
                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        return
                    }

                    // Draw the background
                    val rect = RectF(
                        itemView.left.toFloat(),
                        itemView.top.toFloat(),
                        itemView.right.toFloat(),
                        itemView.bottom.toFloat()
                    );
                    clearPaint.style = Paint.Style.FILL
                    clearPaint.color = Color.parseColor("#64dd17")
                    c.drawRect(rect, clearPaint)


                    // Calculate position icon
                    val doneIconTop = itemView.top + (itemHeight - doneIntrinsicHeight) / 2
                    val doneIconMargin = (itemHeight - doneIntrinsicHeight) / 2
                    val doneIconLeft = itemView.left + doneIconMargin
                    val doneIconRight = itemView.left + doneIconMargin + doneIntrinsicWidth
                    val doneIconBottom = doneIconTop + doneIntrinsicHeight

                    // Draw the delete icon
                    doneIcon!!.setBounds(doneIconLeft, doneIconTop, doneIconRight, doneIconBottom)
                    doneIcon.draw(c)

                }
                if (dX < 0) {
                    if (isCanceled) {
                        clearCanvas(
                            c,
                            itemView.right + dX,
                            itemView.top.toFloat(),
                            itemView.right.toFloat(),
                            itemView.bottom.toFloat()
                        )
                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        return
                    }

                    // Draw the red delete background
                    val rect = RectF(
                        itemView.left.toFloat(),
                        itemView.top.toFloat(),
                        itemView.right.toFloat(),
                        itemView.bottom.toFloat()
                    );
                    clearPaint.style = Paint.Style.FILL
                    clearPaint.color = Color.parseColor("#ff1744")
                    c.drawRect(rect, clearPaint)


                    // Calculate position of delete icon
                    val deleteIconTop = itemView.top + (itemHeight - deleteIntrinsicHeight) / 2
                    val deleteIconMargin = (itemHeight - deleteIntrinsicHeight) / 2
                    val deleteIconLeft = itemView.right - deleteIconMargin - deleteIntrinsicWidth
                    val deleteIconRight = itemView.right - deleteIconMargin
                    val deleteIconBottom = deleteIconTop + deleteIntrinsicHeight

                    // Draw the delete icon
                    deleteIcon!!.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
                    deleteIcon.draw(c)
                }
            }



            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }

        private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
            c?.drawRect(left, top, right, bottom, clearPaint)
        }

    }


}






