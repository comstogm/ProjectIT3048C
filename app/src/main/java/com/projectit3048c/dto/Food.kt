package com.projectit3048c.dto

import com.google.gson.annotations.SerializedName

data class Food(@SerializedName("id") val id: Int=0, @SerializedName("name") val name: String="", @SerializedName("description") val description: String="", @SerializedName("calories") val calories: Int=0) {
    //@SerializedName("description") val description: String="", @SerializedName("calories") val calories: Int?){

    override fun toString(): String {
        return "$name"
    }
}