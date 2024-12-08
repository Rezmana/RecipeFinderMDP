package com.example.recipefinder.ui.createrecipe

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipefinder.DialogLoadingFragment
import com.example.recipefinder.entities.Recipe
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

class CreateRecipeViewModel(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
) : ViewModel() {
//    private val auth = FirebaseAuth.getInstance()
//    private val db = FirebaseFirestore.getInstance()
//    private val storage = FirebaseStorage.getInstance()
    private lateinit var loadingDialog : DialogLoadingFragment

    private val _ingredients = MutableLiveData<List<String>>(emptyList())
    val ingredients: LiveData<List<String>> get() = _ingredients

    private val _prepSteps = MutableLiveData<List<String>>(emptyList())
    val prepSteps: LiveData<List<String>> get() = _prepSteps

    private val _cookingSteps = MutableLiveData<List<String>>(emptyList())
    val cookingSteps: LiveData<List<String>> get() = _cookingSteps

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> get() = _toastMessage

    fun addIngredient(ingredient: String) {
        val updatedIngredients = _ingredients.value.orEmpty() + ingredient
//        _ingredients.value = updatedIngredients
        _ingredients.postValue(updatedIngredients) // Use postValue
    }

    fun addPrepStep(step: String) {
        val updatedSteps = _prepSteps.value.orEmpty() + step
//        _prepSteps.value = updatedSteps
        _prepSteps.postValue(updatedSteps) // Use postValue
    }

    fun addCookingStep(step: String) {
        val updatedSteps = _cookingSteps.value.orEmpty() + step
//        _cookingSteps.value = updatedSteps
        _cookingSteps.postValue(updatedSteps) // Use postValue
    }

    fun saveRecipe(recipeName: String, recipeDescription: String, imageUri: Uri?, typeCuisine: String, difficulty: String) {
        if (recipeName.isEmpty() || recipeDescription.isEmpty()) {
//            _toastMessage.value = "Please fill in all required fields"
            _toastMessage.postValue("Please fill in all required fields")
            return
        }

        if (_ingredients.value.isNullOrEmpty()) {
//            _toastMessage.value = "Please add at least one ingredient"
//            return
            _toastMessage.postValue("Please add at least one ingredient")
            return
        }

        if (_prepSteps.value.isNullOrEmpty() || _cookingSteps.value.isNullOrEmpty()) {
//            _toastMessage.value = "Please add at least one step for both preparation and cooking"
//            return
            _toastMessage.postValue("Please add at least one step for both preparation and cooking")
            return
        }

//        _isLoading.value = true
        _isLoading.postValue(true)
//        viewModelScope.launch {
//            val imageUrl = imageUri?.let { uploadImage(it) }
//            saveRecipeToFirestore(recipeName, recipeDescription, imageUrl, typeCuisine, difficulty)
//            _isLoading.value = false
//        }
        viewModelScope.launch(Dispatchers.IO) {
            val imageUrl = imageUri?.let { uploadImage(it) }
            saveRecipeToFirestore(recipeName, recipeDescription, imageUrl, typeCuisine, difficulty)
            _isLoading.postValue(false)
        }
    }

    private suspend fun uploadImage(uri: Uri): String? {
        return try {
            val filename = "recipe_image_${UUID.randomUUID()}.jpg"
            val imageRef = storage.reference.child("recipe_images/$filename")
            imageRef.putFile(uri).await()
            imageRef.downloadUrl.await().toString()
        } catch (e: Exception) {
            Log.e("CreateRecipeViewModel", "Image upload failed", e)
            _toastMessage.value = "Image upload failed: ${e.localizedMessage}"
            null
        }
    }

    private suspend fun saveRecipeToFirestore(recipeName: String, recipeDescription: String, imageUrl: String?, typeCuisine: String, difficulty: String) {
        try {

            val recipe = Recipe(
                id = UUID.randomUUID().toString(),
                userName = auth.currentUser?.displayName ?: "Unknown User",
                recipeName = recipeName,
                recipeDescription = recipeDescription,
                ingredients = _ingredients.value.orEmpty(),
                prepSteps = _prepSteps.value.orEmpty(),
                cookSteps = _cookingSteps.value.orEmpty(),
                imageUri = imageUrl,
                typeCuisine = typeCuisine,
                difficulty = difficulty,
                userId = auth.currentUser?.uid.orEmpty()
            )

            db.collection("recipes").document(recipe.id).set(recipe).await()
//            _toastMessage.value = "Recipe saved successfully!"
            _toastMessage.postValue( "Recipe saved successfully!")
        } catch (e: Exception) {
            Log.e("CreateRecipeViewModel", "Error saving recipe", e)
//            _toastMessage.value = "Error saving recipe: ${e.localizedMessage}"
            _toastMessage.postValue( "Error saving recipe: ${e.localizedMessage}")
        }
    }

    fun clearToastMessage() {
        _toastMessage.value = null
    }
}
