package com.example.recipefinder.ui.managerecipes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipefinder.entities.Recipe
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ManageRecipesViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    private val _recipes = MutableLiveData<List<Recipe>>()
    val recipes: LiveData<List<Recipe>> get() = _recipes

    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> get() = _toastMessage

    fun fetchUserRecipes() {
        if (userId == null) return

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val querySnapshot = firestore.collection("recipes")
                    .whereEqualTo("userId", userId)
                    .get()
                    .await()

                val userRecipes = querySnapshot.toObjects(Recipe::class.java)
                _recipes.postValue(userRecipes)
            } catch (e: Exception) {
                _toastMessage.postValue("Failed to fetch user recipes: ${e.localizedMessage}")
            }
        }
    }

    fun deleteRecipe(recipe: Recipe) {
        if (userId == null) return

        viewModelScope.launch(Dispatchers.IO) {
            try {
                firestore.collection("recipes").document(recipe.id).delete().await()
                _toastMessage.postValue("Recipe deleted successfully")
                fetchUserRecipes() // Refresh the list after deletion
            } catch (e: Exception) {
                _toastMessage.postValue("Failed to delete recipe: ${e.localizedMessage}")
            }
        }
    }

    fun deleteUserRecipe(recipeId: String) {
        if (userId == null) return

        viewModelScope.launch(Dispatchers.IO) {
            try {
                firestore.collection("recipes").document(recipeId).delete().await()
                _toastMessage.postValue("Recipe deleted successfully")
                fetchUserRecipes()
            } catch (e: Exception) {
                _toastMessage.postValue("Failed to delete recipe: ${e.localizedMessage}")
            }
        }
    }

    fun clearToastMessage() {
        _toastMessage.value = null
    }
}

