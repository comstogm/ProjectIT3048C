package com.projectit3048c.service

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
            val foods = async {service?.getAllFoods()}
            var result = foods.await()?.awaitResponse()?.body()
            return@withContext result
        }
    }
}