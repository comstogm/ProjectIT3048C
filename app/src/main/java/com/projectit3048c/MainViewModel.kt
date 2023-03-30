package com.projectit3048c

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.storage.FirebaseStorage
import com.projectit3048c.dto.Food
import com.projectit3048c.dto.FoodAmount
import com.projectit3048c.dto.Photo
import com.projectit3048c.dto.User
import kotlinx.coroutines.launch
import com.projectit3048c.service.FoodService
import com.projectit3048c.service.IFoodService

class MainViewModel(var foodService : IFoodService =  FoodService()) : ViewModel() {
    val photos: ArrayList<Photo> by mutableStateOf(ArrayList())
    var foods: MutableLiveData<List<Food>> = MutableLiveData<List<Food>>()
    var foodAmounts: MutableLiveData<List<FoodAmount>> = MutableLiveData<List<FoodAmount>>()
    var selectedFoodAmount by mutableStateOf(FoodAmount())
    val NEW_FOODAMOUNT = "New Food"
    var user: User? = null
    val eventPhotos : MutableLiveData<List<Photo>> = MutableLiveData<List<Photo>>()
    private var firestore : FirebaseFirestore = FirebaseFirestore.getInstance()
    private var storageReference = FirebaseStorage.getInstance().reference
    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }

    fun listenToFoodSpecimens() {
        user?.let{
            user ->
            firestore.collection("users").document(user.uid).collection("specimens").addSnapshotListener {
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
                        val foodSpecimen = it.toObject(FoodAmount::class.java)
                        foodSpecimen?.let {
                            allFoodSpecimens.add(it)
                        }
                    }
                    foodAmounts.value = allFoodSpecimens
                }
            }
        }
    }

    fun fetchFoods() {
        viewModelScope.launch {
           val innerFoods = foodService.fetchFoods()
           foods.postValue(innerFoods!!)
        }
    }
    internal fun deleteSavedFoodDatabase(foodAmounts: FoodAmount){
        // TODO:  
    }

    fun saveFoodAmount() {
        user?.let {
            user ->
            val document = if (selectedFoodAmount.foodId.isEmpty()) {
                //create new FoodAmount
                firestore.collection("users").document(user.uid).collection("specimens").document()
            } else {
                //update existing FoodAmount
                firestore.collection("users").document(user.uid).collection("specimens").document(selectedFoodAmount.foodId)
            }
            selectedFoodAmount.foodId = document.id
            val handle = document.set(selectedFoodAmount)
            handle.addOnSuccessListener {
                Log.d("Firebase", "Document Saved")
                if (photos.isNotEmpty()) {
                    uploadPhotos()
                }

            }
            handle.addOnFailureListener { Log.e("Firebase", "Save failed $it ") }
        }
    }

    private fun uploadPhotos() {
        photos.forEach {
            photo ->
            val uri = Uri.parse(photo.localUri)
            // nullable user entry creates technical debt. Wrap in let or enforce user on activity side.
            val imageRef = storageReference.child("images/${user?.uid}/${uri.lastPathSegment}")
            val uploadTask = imageRef.putFile(uri)
            uploadTask.addOnSuccessListener {
                Log.i(TAG, "Image Uploaded $imageRef")
                val downloadUrl = imageRef.downloadUrl
                downloadUrl.addOnSuccessListener {
                    remoteUri ->
                    photo.remoteUri = remoteUri.toString()
                    updatePhotoDatabase(photo)
                }
            }
            uploadTask.addOnFailureListener {
                Log.e(TAG, it.message ?: "No Message")
            }
        }
    }

    internal fun updatePhotoDatabase(photo: Photo) {
        user?.let {
            user ->
            val photoDocument = if (photo.id.isEmpty()) {
                firestore.collection("users").document(user.uid).collection("specimens").document(selectedFoodAmount.foodId).collection("photos").document()
            } else {
                firestore.collection("users").document(user.uid).collection("specimens").document(selectedFoodAmount.foodId).collection("photos").document(photo.id)
            }
            photo.id = photoDocument.id
            val handle = photoDocument.set(photo)
            handle.addOnSuccessListener {
                Log.i(TAG, "Successfully update photo metadata")
                firestore.collection("users").document(user.uid).collection("specimens").document(selectedFoodAmount.foodId).collection("photos").document(photo.id).set(photo)
            }
            handle.addOnFailureListener {
                Log.e(TAG, "Error updating photo data: ${it.message}")
            }
        }
    }

    fun saveUser() {
        user?.let{
            user ->
            val handle = firestore.collection("users").document(user.uid).set(user)
            handle.addOnSuccessListener { Log.d("Firebase", "Document Saved") }
            handle.addOnFailureListener { Log.e("Firebase", "Save failed $it ") }
        }
    }

    fun fetchPhotos() {
        photos.clear()
        user?.let {
            user ->
            val photoCollection = firestore.collection("users").document(user.uid).collection("specimens").document(selectedFoodAmount.foodId).collection("photos")
            var photosListener = photoCollection.addSnapshotListener {
                querySnapshot, firebaseFirestoreException ->
                querySnapshot?.let {
                    querySnapshot ->
                    val documents = querySnapshot.documents
                    documents.forEach {
                        val photo = it.toObject(Photo::class.java)
                        photo?.let {
                            photos.add(photo)
                        }
                    }
                }
            }
        }
    }
}