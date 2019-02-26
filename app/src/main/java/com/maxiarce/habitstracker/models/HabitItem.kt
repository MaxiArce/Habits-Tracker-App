package com.maxiarce.habitstracker.models


class HabitItem{

    var id : Int = 0
    var habitText : String = ""
    var habitDescription: String = ""
    var color: String = ""
    var status: Int = 0
    var days: Int = 0
    var dateStamp:Long =  System.currentTimeMillis()
    var type:Int = 0


    constructor(habitText : String, habitDescription: String ,color: String, status: Int,type: Int){
        this.habitText = habitText
        this.habitDescription = habitDescription
        this.color = color
        this.status = status
        this.type = type
    }

    constructor(){
        this.id = id
        this.habitText = habitText
        this.habitDescription = habitDescription
        this.color = color
        this.status = status
        this.days = days
        this.type =type
    }

}

