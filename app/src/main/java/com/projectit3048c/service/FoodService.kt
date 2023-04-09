package com.projectit3048c.service

import android.util.Log
import com.projectit3048c.RetrofitClientInstance
import com.projectit3048c.dao.IFoodDAO
import com.projectit3048c.dto.Food
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

interface IFoodService {
    suspend fun fetchFoods() : List<Food>?
}
class FoodService : IFoodService {
    override suspend fun fetchFoods() : List<Food>? {
        return withContext(Dispatchers.IO) {
            val service = RetrofitClientInstance.retrofitInstance?.create(IFoodDAO::class.java)
            if (service == null) {
                Log.e("FoodService", "Service is null")
                return@withContext null
            }
            val foods = async {service.getAllFoods()}
            val response = foods.await()?.awaitResponse()
            if (response == null || !response.isSuccessful) {
                Log.e("FoodService", "Error fetching food data from API - response was null or unsuccessful")
                return@withContext null
            }
            val result = response.body()
            if (result == null) {
                Log.e("FoodService", "Null result returned while fetching food data")
                return@withContext null
            }
            return@withContext result
        }
    }
}