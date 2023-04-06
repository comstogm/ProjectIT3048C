package com.projectit3048c.dao

import com.projectit3048c.dto.Food
import retrofit2.Call
import retrofit2.http.GET

/**
 * Interface for accessing food data from an API
 */
interface IFoodDAO {
    /**
     * Returns a list of all available foods
     *
     * @return A [Call] object that can be used to make an asynchronous network request
     */
    @GET("/anastasiiaef/JSON/food")
    fun getAllFoods() : Call<ArrayList<Food>>
}