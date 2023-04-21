package com.projectit3048c.dto

import com.google.gson.annotations.SerializedName

/**
 * Data class representing a type of food
 *
 * @property id ID of the food
 * @property name Name of food
 * @property description Description of food
 * @property calories Number of calories in food
 */
data class Food(var id: Int = 0,
                var name: String = "",
                var description: String= "",
                var calories: String= "" ) {
    override fun toString(): String {
        return "$name $description $calories Cal"
    }
}