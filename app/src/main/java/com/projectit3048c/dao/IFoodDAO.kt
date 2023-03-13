package com.projectit3048c.dao

import com.projectit3048c.dto.Food
import retrofit2.Call
import retrofit2.http.GET

interface IFoodDAO {
    @GET("/anastasiiaef/JSON/food")
    fun getAllFoods() : Call<ArrayList<Food>>
}