package com.projectit3048c.dto

data class FoodAmount(var foodId: String = "", //firebase identifier
                      var foodName: String = "",
                      var internalFoodID: Int = 0,
                      var foodIntake: String = "",
                      var foodAmount: String = "",
                      var foodDate: String = "",
                      var foodCkalories: Int = 0) {
    override fun toString(): String {
        return "$foodName"
    }
}