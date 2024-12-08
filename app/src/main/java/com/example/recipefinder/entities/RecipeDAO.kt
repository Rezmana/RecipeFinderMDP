package com.example.recipefinder.entities

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RecipeDAO {
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun saveRecipe(recipe: Recipe)

        @Query("SELECT * FROM recipes WHERE id = :id")
        suspend fun getRecipeById(id: String): Recipe?

        @Query("SELECT * FROM recipes")
        suspend fun getAllRecipes(): List<Recipe>
        // Method to delete a recipe
        @Delete
        suspend fun deleteRecipe(recipe: Recipe)
    }
