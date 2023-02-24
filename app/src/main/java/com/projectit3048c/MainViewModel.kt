package com.projectit3048c

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projectit3048c.dto.Food
import com.projectit3048c.dto.FoodItems
import kotlinx.coroutines.launch
import com.projectit3048c.service.FoodService

class MainViewModel(var foodService : FoodService =  FoodService()) : ViewModel() {

    var foods : MutableLiveData<Food?> = MutableLiveData<Food?>()


    fun fetchFoods() {
        viewModelScope.launch {
            var innerFoods = foodService.fetchFoods()
            foods.postValue(innerFoods)
        }

    }
    internal fun deleteSavedFoodDatabase(foodItems: FoodItems){
        // TODO:  
    }
}