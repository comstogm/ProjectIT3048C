package com.projectit3048c.dto

import com.google.gson.annotations.SerializedName

data class Food(var id: Int=0, var name: String="", var description: String="", var calories: Float = 0.0f) {
    override fun toString(): String {
        return "$name $description"
    }
}