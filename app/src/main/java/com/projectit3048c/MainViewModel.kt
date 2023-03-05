package com.projectit3048c

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.projectit3048c.dto.Food
import com.projectit3048c.dto.FoodAmount
import com.projectit3048c.dto.FoodItems
import kotlinx.coroutines.launch
import com.projectit3048c.service.FoodService
import com.projectit3048c.service.IFoodService

class MainViewModel(var foodService : IFoodService =  FoodService()) : ViewModel() {

    var foods: MutableLiveData<List<Food>> = MutableLiveData<List<Food>>()
    var foodAmounts: MutableLiveData<List<FoodAmount>> = MutableLiveData<List<FoodAmount>>()

    private lateinit var firestore : FirebaseFirestore

    init {
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }
    fun fetchFoods() {
        viewModelScope.launch {
           var innerFoods = foodService.fetchFoods()
           foods.postValue(innerFoods!!)
        }
    }
    internal fun deleteSavedFoodDatabase(foodItems: FoodItems){
        // TODO:  
    }

    fun save(specimen: FoodAmount) {
        val document = if (specimen.foodId == null || specimen.foodId.isEmpty()) {
            //create new FoodAmount
            firestore.collection("FoodAmount").document()
        } else {
            //update existing FoodAmount
            firestore.collection("specimens").document(specimen.foodId)
        }
        specimen.foodId = document.id
        val handle = document.set(specimen)
        handle.addOnSuccessListener { Log.d("Firebase", "Document Saved") }
        handle.addOnFailureListener { Log.e("Firebase", "Save failed $it ") }
    }
}