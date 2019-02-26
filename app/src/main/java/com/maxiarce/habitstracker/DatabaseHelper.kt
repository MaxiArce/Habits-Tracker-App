package com.maxiarce.habitstracker

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import com.maxiarce.habitstracker.models.HabitItem


val DATADABASE_NAME = "DB"
val TABLE_NAME = "habitstable"
val COL_ID = "id"
val COL_HABIT_TEXT = "habits"
val COL_HABIT_DESCRIPTION = "habitsdescription"
val COL_HABIT_COLOR = "habitcolor"
val COL_STATUS = "status"
val COL_DAYS = "dayscount"
val COL_DATE_STAMP = "daystamp"
val COL_TYPE = "calendartype"

class DatabaseHelper(var context: Context): SQLiteOpenHelper(context, DATADABASE_NAME,null,1){
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE " + TABLE_NAME +" (" + COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT," + COL_HABIT_TEXT + " TEXT," + COL_HABIT_DESCRIPTION + " TEXT," + COL_HABIT_COLOR +" INTEGER,"+ COL_STATUS +" INTEGER,"+ COL_DAYS+" INTEGER,"+ COL_DATE_STAMP +" INTEGER, "+ COL_TYPE +" INTEGER)"
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
        contVal.put(COL_DATE_STAMP,habitItem.dateStamp)
        contVal.put(COL_TYPE,habitItem.type)

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
                habitItem.dateStamp = result.getString(result.getColumnIndex(COL_DATE_STAMP)).toLong()
                habitItem.type = result.getString(result.getColumnIndex(COL_TYPE)).toInt()
                list.add(habitItem)
            }while (result.moveToNext())
        }

        result.close()
        db.close()
        return list

    }

    fun deleteData(id: Int){
        val db = this.writableDatabase

        db.delete(TABLE_NAME,COL_ID+"=? ", arrayOf(id.toString()))

        db.close()
    }


    fun updateDataDays(id: Int, days :Int){
        val db = this.writableDatabase
        var cv =  ContentValues()
        cv.put(COL_DAYS,days+1)
        cv.put(COL_STATUS,1)
        cv.put(COL_DATE_STAMP,System.currentTimeMillis())
        db.update(TABLE_NAME,cv, COL_ID+"=? ", arrayOf(id.toString()))
        db.close()

    }

    fun updateDataStatus(id: Int,status:Int){
        val db = this.writableDatabase
        var cv =  ContentValues()
        cv.put(COL_STATUS,status)
        cv.put(COL_DATE_STAMP,System.currentTimeMillis())
        db.update(TABLE_NAME,cv, COL_ID+"=? ", arrayOf(id.toString()))
        db.close()

    }
}

