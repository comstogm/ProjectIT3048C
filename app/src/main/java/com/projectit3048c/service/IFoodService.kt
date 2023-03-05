package com.projectit3048c.service

import com.projectit3048c.dto.Food

interface IFoodService {
    suspend fun fetchFoods(): List<Food>?
}