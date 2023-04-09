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
                      var foodAmount: String = "",
                      var foodDate: String = "") {
    init {
        if (internalFoodID < 0) throw IllegalArgumentException("Internal food ID must be greater than or equal to 0.")
    }

    /**
     * Returns a string of food and its intake amount
     *
     * @return String made from the name/amount/date of the food
     */
    override fun toString(): String {
        return "$foodName $foodAmount $foodDate"
    }
}