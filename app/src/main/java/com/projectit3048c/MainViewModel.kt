package com.projectit3048c

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.projectit3048c.dto.Food
import com.projectit3048c.dto.FoodAmount
import kotlinx.coroutines.launch
import com.projectit3048c.service.FoodService
import com.projectit3048c.service.IFoodService

class MainViewModel(var foodService : IFoodService =  FoodService()) : ViewModel() {

    var foods: MutableLiveData<List<Food>> = MutableLiveData<List<Food>>()
    var foodAmounts: MutableLiveData<List<FoodAmount>> = MutableLiveData<List<FoodAmount>>()
    var selectedFoodAmount by mutableStateOf(FoodAmount())
    val NEW_FOODAMOUNT = "New Food"

    private lateinit var firestore : FirebaseFirestore

    init {
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        listenToFoodSpecimens()
    }



    private fun listenToFoodSpecimens() {
        firestore.collection("specimens").addSnapshotListener {
                snapshot, e ->
            //handle error
            if(e != null) {
                Log.w("Listen failed", e)
                return@addSnapshotListener
            }
            //If we made it here, there is no error
            snapshot?.let {
                val allFoodSpecimens = ArrayList<FoodAmount>()
                allFoodSpecimens.add(FoodAmount(foodName = NEW_FOODAMOUNT))
                val documents = snapshot.documents
                documents.forEach {
                    val loggedFood = it.toObject(FoodAmount::class.java)
                    loggedFood?.let {
                        allFoodSpecimens.add(it)
                    }
                }
                foodAmounts.value = allFoodSpecimens
            }
        }
    }

    fun fetchFoods() {
        viewModelScope.launch {
           var innerFoods = foodService.fetchFoods()
           foods.postValue(innerFoods!!)
        }
    }
    internal fun deleteSavedFoodDatabase(foodAmount: FoodAmount){
        // TODO:  
    }

    fun saveFoodAmount(loggedFood: FoodAmount) {
        val document = if (selectedFoodAmount.foodId == null || selectedFoodAmount.foodId.isEmpty()) {
            //create new FoodAmount
            firestore.collection("specimens").document()
        } else {
            //update existing FoodAmount
            firestore.collection("specimens").document(selectedFoodAmount.foodId)
        }
        selectedFoodAmount.foodId = document.id
        val handle = document.set(selectedFoodAmount)
        handle.addOnSuccessListener { Log.d("Firebase", "Document Saved") }
        handle.addOnFailureListener { Log.e("Firebase", "Save failed $it ") }

    }
}