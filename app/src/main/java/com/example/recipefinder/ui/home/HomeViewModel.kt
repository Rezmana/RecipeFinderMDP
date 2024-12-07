package com.example.recipefinder.ui.home

import android.util.Log
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
import java.util.Calendar

class HomeViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _greeting = MutableLiveData<String>()
    val greeting: LiveData<String> get() = _greeting

    private val _featuredRecipes = MutableLiveData<List<Recipe>>()
    val featuredRecipes: LiveData<List<Recipe>> get() = _featuredRecipes

    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> get() = _toastMessage

    init {
        fetchFeaturedRecipes()
        updateGreeting()
    }

    private fun fetchFeaturedRecipes() {
        firestore.collection("recipes")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val recipes = querySnapshot.toObjects(Recipe::class.java)
                _featuredRecipes.value = recipes
                Log.d("HomeViewModel", "Fetched recipes: ${recipes.size}")
            }
            .addOnFailureListener { exception ->
                Log.e("HomeViewModel", "Error fetching recipes: ${exception.message}")
            }
    }

    private fun updateGreeting() {
        val user = auth.currentUser
        val greetingText = when (getCurrentTimeOfDay()) {
            "morning" -> "Good Morning"
            "afternoon" -> "Good Afternoon"
            "evening" -> "Good Evening"
            else -> "Hello"
        }

        if (user != null) {
            val userId = user.uid
            firestore.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    val username = document.getString("username") ?: "User"
                    _greeting.postValue("$greetingText, $username!")
                }
                .addOnFailureListener {
                    _greeting.postValue("$greetingText, User!") // Fallback
                }
        } else {
            _greeting.postValue("$greetingText, User!") // Fallback
        }
    }

    private fun getCurrentTimeOfDay(): String {
        return when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
            in 0..11 -> "morning"
            in 12..16 -> "afternoon"
            in 17..23 -> "evening"
            else -> "day"
        }
    }

    fun clearToastMessage() {
        _toastMessage.value = null
    }
}
