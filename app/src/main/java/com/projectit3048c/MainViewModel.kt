package com.projectit3048c

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projectit3048c.dto.Food
import com.projectit3048c.dto.FoodItems
import kotlinx.coroutines.launch
import com.projectit3048c.service.FoodService
import com.projectit3048c.service.IFoodService

class MainViewModel(var foodService : IFoodService =  FoodService()) : ViewModel() {

    var foods: MutableLiveData<List<Food>?> = MutableLiveData<List<Food>?>()


    fun fetchFoods() {
        viewModelScope.launch {
            var innerFoods = foodService.fetchFoods()
            foods.postValue(innerFoods)
            //foodService.fetchFoods()
        }

    }
    internal fun deleteSavedFoodDatabase(foodItems: FoodItems){
        // TODO:  
    }

    //fun getSearchResult(query: String) {
      //  viewModelScope.launch {
          //  var innerFoods = foodService.getSearchResult(query)
           // foods.postValue(innerFoods)
        //}
    //}
}