package com.projectit3048c.dto

import com.google.gson.annotations.SerializedName

data class Food(var id: Int = 0,
                var name: String = "",
                var description: String= "",
                var calories: String= "" ) {
    override fun toString(): String {
        return "$name $description $calories Cal"
    }
}
