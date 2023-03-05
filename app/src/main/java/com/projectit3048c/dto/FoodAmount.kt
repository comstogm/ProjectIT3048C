package com.projectit3048c.dto

data class FoodAmount(var foodId: String = "", var foodName : String = "", var foodIntake : String = "", var foodAmount : String = "", var foodLogged : String = "") {
    override fun toString(): String {
        return "$foodId $foodName $foodAmount $foodLogged"
    }
}