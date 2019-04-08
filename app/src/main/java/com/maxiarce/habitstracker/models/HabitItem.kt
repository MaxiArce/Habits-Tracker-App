package com.maxiarce.habitstracker.models


class HabitItem{

    var id : Int = 0
    var habitText : String = ""
    var habitDescription: String = ""
    var color: String = ""
    var status: Int = 0
    var days: Int = 0
    var randomDaysReveal = ""
    var dateStamp:Long =  System.currentTimeMillis()
    var type:Int = 0
    var daysInRow = 0
    var daysInRowTotal = 0
    var daysOutOfRow = 0


    constructor(habitText : String, habitDescription: String ,color: String, status: Int,randomDays: String,type: Int){
        this.habitText = habitText
        this.habitDescription = habitDescription
        this.color = color
        this.status = status
        this.randomDaysReveal = randomDays
        this.type = type
    }

    constructor(){
        this.id = id
        this.habitText = habitText
        this.habitDescription = habitDescription
        this.color = color
        this.status = status
        this.days = days
        this.randomDaysReveal = randomDaysReveal
        this.type =type
        this.daysInRow = daysInRow
        this.daysInRowTotal = daysInRowTotal
        this.daysOutOfRow = daysOutOfRow
    }

}

