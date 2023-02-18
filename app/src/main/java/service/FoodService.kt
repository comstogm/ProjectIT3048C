package service

import RetrofitClientInstance
import dao.iFoodDAO
import dto.Food
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

class FoodService {
    suspend fun fetchFoods(): Food? {
        return withContext(Dispatchers.IO) {
            val service = RetrofitClientInstance.retrofitInstance?.create(iFoodDAO::class.java)
            val foods = async {service?.getAllFoods()}
            var result = foods.await()?.awaitResponse()?.body()
            return@withContext result
        }
    }
}