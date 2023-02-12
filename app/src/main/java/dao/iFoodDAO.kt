package dao

import dto.Food
import retrofit2.Call
import retrofit2.http.GET

interface iFoodDAO {

    @GET("/perl/mobile/viewplantsjsonarray.pl")
    fun getAllFoods() : Call<ArrayList<Food>>
}