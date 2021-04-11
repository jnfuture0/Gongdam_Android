package org.gongdam.Struct

class Board3String1Int(val image : String,
                       val name : String,
                       val content : String,
                       val room_id : Int) {
}

data class BoardTimeList(val cost : Int, var newCost:Int, val time:String, val endTime:String, var isChecked:Boolean, var isShow:Boolean, var type:Int)