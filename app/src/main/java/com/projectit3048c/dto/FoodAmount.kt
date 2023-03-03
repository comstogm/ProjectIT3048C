package com.projectit3048c.dto

data class FoodAmount(var foodId : Int = 0, var foodName : String = "", var foodIntake : String = "", var foodAmount : String = "", var foodLoged : String = "") {
    override fun toString(): String {
        return "$foodId $foodName $foodAmount $foodLoged"
    }
}