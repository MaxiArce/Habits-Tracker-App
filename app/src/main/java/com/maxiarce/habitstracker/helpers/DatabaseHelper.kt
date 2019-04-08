package com.maxiarce.habitstracker.helpers

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import com.maxiarce.habitstracker.models.HabitItem
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


val DATADABASE_NAME = "DB"
val TABLE_NAME = "habitstable"
val COL_ID = "id"
val COL_HABIT_TEXT = "habits"
val COL_HABIT_DESCRIPTION = "habitsdescription"
val COL_HABIT_COLOR = "habitcolor"
val COL_STATUS = "status"
val COL_DAYS = "dayscount"
val COL_RANDOM_DAYS = "randomdays"
val COL_DATE_STAMP = "daystamp"
val COL_TYPE = "calendartype"
val COL_DAYS_IN_ROW = "daysinrow"
val COL_DAYS_IN_ROW_TOTAL = "daysinrowtotal"
val COL_DAYS_OUT_OF_ROW = "daysoutofrow"

class DatabaseHelper(var context: Context): SQLiteOpenHelper(context,
    DATADABASE_NAME,null,1){
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE " + TABLE_NAME +" (" + COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT," + COL_HABIT_TEXT + " TEXT," + COL_HABIT_DESCRIPTION + " TEXT," + COL_HABIT_COLOR +" INTEGER,"+ COL_STATUS +" INTEGER,"+ COL_DAYS +" INTEGER,"+ COL_RANDOM_DAYS +" TEXT," + COL_DATE_STAMP +" INTEGER, " + COL_TYPE +" INTEGER," + COL_DAYS_IN_ROW + " INTEGER, "+ COL_DAYS_IN_ROW_TOTAL+ " INTEGER, "+ COL_DAYS_OUT_OF_ROW +" INTEGER )"
        db?.execSQL(createTable)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    fun insertData(habitItem : HabitItem){
        val db = this.writableDatabase
        var contVal = ContentValues()

        contVal.put(COL_HABIT_TEXT,habitItem.habitText)
        contVal.put(COL_HABIT_DESCRIPTION,habitItem.habitDescription)
        contVal.put(COL_HABIT_COLOR,habitItem.color)
        contVal.put(COL_STATUS,habitItem.status)
        contVal.put(COL_DAYS,habitItem.days)
        contVal.put(COL_RANDOM_DAYS,habitItem.randomDaysReveal)
        contVal.put(COL_DATE_STAMP,habitItem.dateStamp)
        contVal.put(COL_TYPE,habitItem.type)
        contVal.put(COL_DAYS_IN_ROW,habitItem.daysInRow)
        contVal.put(COL_DAYS_IN_ROW_TOTAL,habitItem.daysInRowTotal)
        contVal.put(COL_DAYS_OUT_OF_ROW,habitItem.daysOutOfRow)


        val result = db.insert(TABLE_NAME,null,contVal)
        if (result == -1.toLong())
        {
            Toast.makeText(context,"Failed",Toast.LENGTH_LONG).show()
        }else{

            Toast.makeText(context,"Sucess",Toast.LENGTH_LONG).show()
        }

        db.close()

    }

    fun readData(): MutableList<HabitItem>{
        var list : MutableList<HabitItem> =  ArrayList()
        val db = this.readableDatabase
        val query = "Select * from " + TABLE_NAME
        val result = db.rawQuery(query,null)

        if (result.moveToFirst()){
            do{
                var habitItem = HabitItem()
                habitItem.id = result.getString(result.getColumnIndex(COL_ID)).toInt()
                habitItem.habitText = result.getString(result.getColumnIndex(COL_HABIT_TEXT))
                habitItem.habitDescription = result.getString(result.getColumnIndex(COL_HABIT_DESCRIPTION))
                habitItem.color = result.getString(result.getColumnIndex(COL_HABIT_COLOR))
                habitItem.status = result.getString(result.getColumnIndex(COL_STATUS)).toInt()
                habitItem.days = result.getString(result.getColumnIndex(COL_DAYS)).toInt()
                habitItem.randomDaysReveal = result.getString(result.getColumnIndex(COL_RANDOM_DAYS))
                habitItem.dateStamp = result.getString(result.getColumnIndex(COL_DATE_STAMP)).toLong()
                habitItem.type = result.getString(result.getColumnIndex(COL_TYPE)).toInt()
                habitItem.daysInRow = result.getString(result.getColumnIndex(COL_DAYS_IN_ROW)).toInt()
                habitItem.daysInRowTotal = result.getString(result.getColumnIndex(COL_DAYS_IN_ROW_TOTAL)).toInt()
                habitItem.daysOutOfRow = result.getString(result.getColumnIndex(COL_DAYS_OUT_OF_ROW)).toInt()

                list.add(habitItem)
            }while (result.moveToNext())
        }

        result.close()
        db.close()
        return list

    }

    fun deleteData(id: Int){
        val db = this.writableDatabase

        db.delete(
            TABLE_NAME,
            COL_ID +"=? ", arrayOf(id.toString()))
        db.close()
    }

    fun searchById(id: Int): HabitItem{
        var habitItem = HabitItem()
        var db = this.readableDatabase
        var query = "Select * from " + TABLE_NAME + " WHERE id = ?"
        var result = db.rawQuery(query, arrayOf(id.toString()))
        println(id)
        if(result.moveToFirst()){
            do{
                habitItem.id = result.getString(result.getColumnIndex(COL_ID)).toInt()
                habitItem.habitText = result.getString(result.getColumnIndex(COL_HABIT_TEXT))
                habitItem.habitDescription = result.getString(result.getColumnIndex(COL_HABIT_DESCRIPTION))
                habitItem.color = result.getString(result.getColumnIndex(COL_HABIT_COLOR))
                habitItem.status = result.getString(result.getColumnIndex(COL_STATUS)).toInt()
                habitItem.days = result.getString(result.getColumnIndex(COL_DAYS)).toInt()
                habitItem.randomDaysReveal = result.getString(result.getColumnIndex(COL_RANDOM_DAYS))
                habitItem.dateStamp = result.getString(result.getColumnIndex(COL_DATE_STAMP)).toLong()
                habitItem.type = result.getString(result.getColumnIndex(COL_TYPE)).toInt()
                habitItem.daysInRow = result.getString(result.getColumnIndex(COL_DAYS_IN_ROW)).toInt()
                habitItem.daysInRowTotal = result.getString(result.getColumnIndex(COL_DAYS_IN_ROW_TOTAL)).toInt()
                habitItem.daysOutOfRow = result.getString(result.getColumnIndex(COL_DAYS_OUT_OF_ROW)).toInt()
            }while (result.moveToNext())
        }
        result.close()
        db.close()
        return habitItem
    }

    fun updateById(item: HabitItem){

        var id = item.id
        var db = this.writableDatabase

        var contVal = ContentValues()
        contVal.put(COL_HABIT_TEXT,item.habitText)
        contVal.put(COL_HABIT_DESCRIPTION,item.habitDescription)
        contVal.put(COL_HABIT_COLOR,item.color)
        contVal.put(COL_STATUS,item.status)
        contVal.put(COL_DAYS,item.days)
        contVal.put(COL_RANDOM_DAYS,item.randomDaysReveal)
        contVal.put(COL_DATE_STAMP,item.dateStamp)
        contVal.put(COL_TYPE,item.type)
        contVal.put(COL_DAYS_IN_ROW,item.daysInRow)
        contVal.put(COL_DAYS_IN_ROW_TOTAL,item.daysInRowTotal)
        contVal.put(COL_DAYS_OUT_OF_ROW,item.daysOutOfRow)


        db.update(
            TABLE_NAME,contVal,
            COL_ID +"=? ", arrayOf(id.toString()))
        db.close()
    }


    fun updateDataDays(habitItem: HabitItem){
        val db = this.writableDatabase
        var contVal =  ContentValues()

        val daydiff = TimeUnit.MILLISECONDS.toDays((System.currentTimeMillis() - habitItem.dateStamp))

        if (daydiff.toInt() ==0 ){
            contVal.put(COL_DAYS_IN_ROW,1)
        }
        if (daydiff.toInt() == 1){
            contVal.put(COL_DAYS_IN_ROW,habitItem.daysInRow +1)
            contVal.put(COL_DAYS_IN_ROW_TOTAL,habitItem.daysInRowTotal+1)
        }
        if (daydiff.toInt() > 1){
            contVal.put(COL_DAYS_IN_ROW,0)
            contVal.put(COL_DAYS_OUT_OF_ROW,habitItem.daysOutOfRow+daydiff)
        }

        contVal.put(COL_DAYS,habitItem.days+1)
        contVal.put(COL_STATUS,1)
        contVal.put(COL_DATE_STAMP,System.currentTimeMillis())

        Log.d("UPDATE",contVal.toString())

        db.update(TABLE_NAME,contVal, COL_ID +"=? ", arrayOf(habitItem.id.toString()))
        db.close()


    }

    fun updateCheckStatusOnStart(id: Int,status:Int){
        val db = this.writableDatabase
        var contVal =  ContentValues()

        contVal.put(COL_STATUS,status)

        db.update(TABLE_NAME,contVal, COL_ID +"=? ", arrayOf(id.toString()))
        db.close()

    }



}


