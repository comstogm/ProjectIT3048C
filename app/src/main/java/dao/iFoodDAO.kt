package dao

import dto.Food
import retrofit2.Call
import retrofit2.http.GET

interface iFoodDAO {

    @GET("fdc/v1/foods/search?query=egg&dataType=Foundation&pageSize=3&pageNumber=1&sortBy=dataType.keyword&sortOrder=asc&api_key=trqHcOXD3WWuC2z5OhYoMiUpZaFp2GFtEcUSs8fA")
    fun getAllFoods() : Call<Food>
}