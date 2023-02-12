package dto

import com.google.gson.annotations.SerializedName

data class Food(@SerializedName("genus") var genus : String, var species : String, var common : String ) {
    override fun toString(): String {
        return common
    }
}
