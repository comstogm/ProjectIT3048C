package com.projectit3048c.dto

import com.google.gson.annotations.SerializedName

data class Food(@SerializedName("id") var id: Int=0, @SerializedName("name") var name: String="", @SerializedName("description") var description: String="", @SerializedName("calories") var calories: Int=0) {

    override fun toString(): String {
        return "$name"
    }
}