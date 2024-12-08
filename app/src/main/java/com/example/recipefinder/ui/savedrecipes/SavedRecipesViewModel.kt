package com.example.recipefinder.ui.savedrecipes

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipefinder.database.RoomDB
import com.example.recipefinder.entities.Recipe
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SavedRecipesViewModel(context: Context) : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private val recipeDao = RoomDB.getDatabase(context).recipeDao()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _savedRecipes = MutableLiveData<List<Recipe>>()
    val savedRecipes: LiveData<List<Recipe>> get() = _savedRecipes

    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> get() = _toastMessage

    init {
        fetchSavedRecipesFromFirestore()
        fetchSavedRecipesFromLocalDB() // Load recipes initially from the local database
    }

    fun fetchSavedRecipesFromFirestore() {
        if (userId == null) {
            _toastMessage.value = "You need to log in to view saved recipes"
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val querySnapshot = firestore.collection("users")
                    .document(userId)
                    .collection("savedRecipes")
                    .get()
                    .await()

                val savedRecipes = querySnapshot.toObjects(Recipe::class.java)
                withContext(Dispatchers.Main) {
                    _savedRecipes.value = savedRecipes
                }
            } catch (e: Exception) {
                _toastMessage.postValue("Failed to load recipes: ${e.localizedMessage}")
            }
        }
    }

    fun fetchSavedRecipesFromLocalDB() {
        viewModelScope.launch(Dispatchers.IO) {
            val savedRecipes = recipeDao.getAllRecipes() // Replace with your DAO method
            if (savedRecipes.isEmpty()) {
                _toastMessage.postValue("No recipes found in local DB")
            } else {
                _savedRecipes.postValue(savedRecipes)
            }
        }
    }

    fun removeSavedRecipe(recipe: Recipe) {
        viewModelScope.launch(Dispatchers.IO) {
            // Remove from local Room database
            recipeDao.deleteRecipe(recipe)

            // Remove from Firestore if applicable
            val userId = auth.currentUser?.uid.orEmpty()
            firestore.collection("users")
                .document(userId)
                .collection("savedRecipes")
                .document(recipe.id)
                .delete()
                .addOnSuccessListener {
                    _toastMessage.postValue("Recipe removed successfully!")
                    fetchSavedRecipesFromLocalDB()
                    fetchSavedRecipesFromFirestore()// Refresh the list
                }
                .addOnFailureListener { exception ->
                    _toastMessage.postValue("Failed to remove recipe: ${exception.message}")
                }
        }
    }

    fun clearToastMessage() {
        _toastMessage.value = null
    }
}
