package com.projectit3048c.dto

data class FoodAmount(var foodId: String = "", //firebase identifier
                      var foodName: String = "", //name
                      var foodIntake: String = "",
                      var foodAmount: String = "",
                      var foodDate: String = "") {
    override fun toString(): String {
        return "$foodId $foodName $foodAmount $foodDate"
    }
}