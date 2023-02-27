package com.projectit3048c.dao

import com.projectit3048c.dto.Food
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface iFoodDAO {

    @GET("/anastasiiaef/JSON/food")
    fun getAllFoods() : Call<ArrayList<Food>>

   // @GET("fdc/v1/foods/search?&dataType=Foundation&pageSize=3&pageNumber=1&sortBy=dataType.keyword&sortOrder=asc&api_key=trqHcOXD3WWuC2z5OhYoMiUpZaFp2GFtEcUSs8fA&")
    //fun getSearchFoods(
       // @Query("query") query: String
    //) : Call<Food>
}