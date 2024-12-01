package com.example.recipefinder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipefinder.entities.Recipe
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SavedRecipes : Fragment() {

    private lateinit var recipeAdapter: RecipeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_saved_recipe, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView and Adapter
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvSavedRecipes)
        recipeAdapter = RecipeAdapter { recipe ->
            // Handle recipe click, e.g., navigate to details
            Toast.makeText(context, "Clicked: ${recipe.recipeName}", Toast.LENGTH_SHORT).show()
        }

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = recipeAdapter

        // Fetch saved recipes and update RecyclerView
        fetchSavedRecipes()
    }

    private fun fetchSavedRecipes() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            Toast.makeText(context, "You need to log in to view saved recipes", Toast.LENGTH_SHORT).show()
            return
        }

        val firestore = FirebaseFirestore.getInstance()
        val savedRecipesRef = firestore.collection("users").document(userId).collection("savedRecipes")

        savedRecipesRef.get()
            .addOnSuccessListener { querySnapshot ->
                val savedRecipes = querySnapshot.toObjects(Recipe::class.java)
                updateRecyclerView(savedRecipes)
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Failed to load recipes: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateRecyclerView(savedRecipes: List<Recipe>) {
        recipeAdapter.updateRecipes(savedRecipes) // Call the `updateRecipes` method in RecipeAdapter
    }
}
