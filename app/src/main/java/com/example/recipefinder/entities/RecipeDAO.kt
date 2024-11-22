//package com.example.recipefinder.entities
//
//import androidx.room.Dao
//import androidx.room.Delete
//import androidx.room.Insert
//import androidx.room.OnConflictStrategy
//import androidx.room.Query
//import androidx.room.Update
//import kotlinx.coroutines.flow.Flow
//
//@Dao
//interface RecipeDao {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertRecipe(recipe: Recipe): Long
//
//    @Query("SELECT * FROM recipe")
//    fun getAllRecipes(): Flow<List<Recipe>>
//
//    @Update
//    suspend fun updateRecipe(recipe: Recipe)
//
//    @Delete
//    suspend fun deleteRecipe(recipe: Recipe)
//
//    @Query("SELECT * FROM recipe WHERE id = :recipeId")
//    suspend fun getRecipeById(recipeId: String): Recipe?
//}
