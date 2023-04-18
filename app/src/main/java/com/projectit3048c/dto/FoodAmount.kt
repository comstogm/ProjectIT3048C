package com.projectit3048c.dto

data class FoodAmount(var foodId: String = "", //firebase identifier
                      var foodName: String = "",
                      var internalFoodID: Int = 0,
                      var foodIntake: String = "",
                      var foodAmount: String = "0",
                      var foodDate: String = "" ) {
    override fun toString(): String {
        return "$foodName"
    }
}