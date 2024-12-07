package com.example.recipefinder.entities

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_recipes")
class RecipeLocal {
    data class RecipeLocal(
        @PrimaryKey val id: String,
        val recipeName: String,
        val ingredients: List<String>,
        val recipeDescription: String,
        val steps: List<String>,
        val imageUri: String?,
        val cookingTime: String? = null,  // Cooking time
        var vegan: Boolean = false,  // Indicates if the recipe is vegan
        var vegetarian: Boolean = false  // Indicates if the recipe is vegetarian

    )
}