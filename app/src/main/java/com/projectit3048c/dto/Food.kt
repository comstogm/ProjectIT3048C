package com.projectit3048c.dto

import com.google.gson.annotations.SerializedName

data class Food(var id: Int=0, var name: String="", var description: String="", var calories: String="") {
    override fun toString(): String {
        return "$name"
    }
    fun toString2(): String {
        return "$name $description"
    }

    


//    companion object {
//        fun getCal(): String {
//            return "$calories"
//        }
//    }
}

//    fun getCal (calories: String): Float {
//        return calories.toFloat()
//    }



//    fun toFloat(): Float {
//        var total = 0F
//        calories.forEach { total += it.consumedCalories }
//        return total
//    }
//    companion object {
//        fun toFloat(calories: String): Float {
//            return calories.toFloat()
//        }
//    }




