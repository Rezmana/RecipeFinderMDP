package com.example.recipefinder.ui.browse

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipefinder.entities.Recipe
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class BrowseViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    private val _recipes = MutableLiveData<List<Recipe>>()
    val recipes: LiveData<List<Recipe>> get() = _recipes

    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> get() = _toastMessage

    init {
        fetchAllRecipes() // Load all recipes initially
    }

    fun fetchAllRecipes() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val querySnapshot = firestore.collection("recipes").get().await()
                val recipeList = querySnapshot.toObjects(Recipe::class.java)
                _recipes.postValue(recipeList)
            } catch (e: Exception) {
                _toastMessage.postValue("Failed to fetch recipes: ${e.localizedMessage}")
            }
        }
    }

    fun searchRecipes(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val querySnapshot = firestore.collection("recipes")
                    .whereGreaterThanOrEqualTo("recipeName", query)
                    .whereLessThanOrEqualTo("recipeName", query + "\uf8ff")
                    .get()
                    .await()

                if (querySnapshot.isEmpty) {
                    _toastMessage.postValue("No recipes found for \"$query\"")
                } else {
                    val recipes = querySnapshot.toObjects(Recipe::class.java)
                    _recipes.postValue(recipes)
                }
            } catch (e: Exception) {
                _toastMessage.postValue("Search failed: ${e.localizedMessage}")
            }
        }
    }

    fun filterRecipesByIngredient(ingredient: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val querySnapshot = firestore.collection("recipes")
                    .whereArrayContains("ingredients", ingredient)
                    .get()
                    .await()

                val filteredRecipes = querySnapshot.toObjects(Recipe::class.java)
                _recipes.postValue(filteredRecipes)
            } catch (e: Exception) {
                _toastMessage.postValue("Failed to filter recipes: ${e.localizedMessage}")
            }
        }
    }

    fun filterRecipesByTag(ingredient: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val querySnapshot = firestore.collection("recipes")
                    .whereArrayContains("ingredients", ingredient)
                    .get()
                    .await()

                val filteredRecipes = querySnapshot.toObjects(Recipe::class.java)
                _recipes.postValue(filteredRecipes)
            } catch (e: Exception) {
                _toastMessage.postValue("Failed to filter recipes: ${e.localizedMessage}")
            }
        }
    }

    fun clearToastMessage() {
        _toastMessage.value = null
    }
}
