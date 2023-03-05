package com.projectit3048c.dto

data class FoodItems(
    var fdcId: String = "",
    var description: String = "",
    var foodNutrients: ArrayList<FoodNutrients> = arrayListOf()
) {
    override fun toString(): String {
        return description
    }
}
