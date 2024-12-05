package com.example.realtimedatabasewithalertbox

import com.google.firebase.database.Exclude

data class ItemData(
    var id : String?= "",
    var stdName : String ?= "",
    var stdClass : String ?="",
    var stdRollNumber : String ?= ""
){
    @Exclude
    fun toMap() : Map<String, Any?>{
        return mapOf(
            "id" to id,
            "stdName" to stdName,
            "stdClass" to stdClass,
            "stdRollNumber" to stdRollNumber
        )
    }
}

