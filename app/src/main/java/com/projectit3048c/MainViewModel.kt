package com.projectit3048c

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projectit3048c.dto.Food
import kotlinx.coroutines.launch
import com.projectit3048c.service.FoodService
import com.projectit3048c.service.IFoodService

class MainViewModel(var foodService : IFoodService =  FoodService()) : ViewModel() {

    var foods : MutableLiveData<Food?> = MutableLiveData<Food?>()


    fun fetchFoods() {
        viewModelScope.launch {
            var innerFoods = foodService.fetchFoods()
            foods.postValue(innerFoods)
        }

    }
}