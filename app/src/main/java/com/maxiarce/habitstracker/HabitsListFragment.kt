package com.maxiarce.habitstracker


import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_habits_list.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class HabitsListFragment : Fragment() {

    var habitsList = mutableListOf<String>()
    init {
        habitsList.add("Chau1")
        habitsList.add("Chau2")
        habitsList.add("Chau3")
        habitsList.add("Chau4")
        habitsList.add("Chau5")
        habitsList.add("Chau6")
        habitsList.add("Chau7")
        habitsList.add("Chau8")
        habitsList.add("Chau9")
        habitsList.add("Chau10")
        habitsList.add("Chau11")
        habitsList.add("Chau12")
        habitsList.add("Chau13")
        habitsList.add("Chau14")
        habitsList.add("Chau15")
        habitsList.add("Chau16")
        habitsList.add("Chau17")
        habitsList.add("Chau18")
        habitsList.add("Chau19")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val rootView= inflater.inflate(R.layout.fragment_habits_list, container, false)

        //set the recycler View
        val recyclerView = rootView.findViewById<RecyclerView>(R.id.recyclerView_habits_dashboard)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = HabitsAdapter(habitsList)



        //set itemTouchHelper for swipe to complete row
        val habitsRowSwipeCallback = HabitsRowSwipe(habitsList,recyclerView.context)
        val itemTouchHelper = ItemTouchHelper(habitsRowSwipeCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        return rootView

    }

    inner class HabitsRowSwipe(val habits : MutableList<String>, context: Context) : ItemTouchHelper.Callback() {

        private val doneIcon = ContextCompat.getDrawable(context, R.drawable.ic_done)
        private val doneIntrinsicWidth = doneIcon!!.intrinsicWidth
        private val doneIntrinsicHeight = doneIcon!!.intrinsicHeight

        private val deleteIcon = ContextCompat.getDrawable(context, R.drawable.ic_delete)
        private val deleteIntrinsicWidth = deleteIcon!!.intrinsicWidth
        private val deleteIntrinsicHeight = deleteIcon!!.intrinsicHeight


        private val clearPaint = Paint()


        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
            val completeFlag = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            return makeMovementFlags(0,completeFlag)

        }

        override fun onMove(recyclerView: RecyclerView, originViewHolder: RecyclerView.ViewHolder, targetVIewHolder: RecyclerView.ViewHolder): Boolean {
            return false

        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            if(direction == ItemTouchHelper.RIGHT ){
                val adapter =  recyclerView_habits_dashboard.adapter  as HabitsAdapter
                adapter.removeRow(viewHolder.adapterPosition)
            }

            if(direction == ItemTouchHelper.LEFT ){
                val adapter =  recyclerView_habits_dashboard.adapter  as HabitsAdapter
                adapter.removeRow(viewHolder.adapterPosition)
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

            if( actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
                //right swipe - done habit
                if (dX >0){

                    if (isCanceled) {
                        clearCanvas(c, itemView.left + dX, itemView.top.toFloat(), itemView.left.toFloat(), itemView.bottom.toFloat())
                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        return
                    }

                    // Draw the  background
                    val rectRound =  RectF(itemView.right.toFloat(),itemView.top.toFloat(),itemView.left.toFloat(),itemView.bottom.toFloat())
                    clearPaint.style = Paint.Style.FILL
                    clearPaint.color = Color.parseColor("#64dd17")
                    c.drawRoundRect(rectRound,25f,25f,clearPaint)


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
                if (dX<0){
                    if (isCanceled) {
                        clearCanvas(c, itemView.right + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        return
                    }

                    // Draw the red delete background
                    val rectRound =  RectF(itemView.right.toFloat(),itemView.top.toFloat(),itemView.left.toFloat(),itemView.bottom.toFloat())
                    clearPaint.style = Paint.Style.FILL
                    clearPaint.color = Color.parseColor("#ff1744")
                    c.drawRoundRect(rectRound,25f,25f,clearPaint)


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
