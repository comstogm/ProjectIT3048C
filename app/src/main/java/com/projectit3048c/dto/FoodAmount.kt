package com.projectit3048c.dto

/**
 * Data class representing an amount of food
 *
 * @property foodId ID of the food
 * @property foodName Name of food
 * @property internalFoodID ID of food from internal database
 * @property description Description of food
 * @property calories Number of calories in food
 */
data class FoodAmount(var foodId: String = "", //firebase identifier
                      var foodName: String = "",
                      var internalFoodID: Int = 0,
                      var foodIntake: String = "",
                      var foodAmount: String = "0",
                      var foodDate: String = "" ) {

    /**
     * This implementation of toString returns the value of the [foodName] property.
     */
    override fun toString(): String {
        return "$foodName"
    }
}