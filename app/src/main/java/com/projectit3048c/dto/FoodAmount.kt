package com.projectit3048c.dto

data class FoodAmount(val foodId : Int = 0, var foodName : String = "", val foodAmount : Int = 0, var dateLoged : String = "") {
    override fun toString(): String {
        return "$foodName $foodAmount $dateLoged"
    }
}