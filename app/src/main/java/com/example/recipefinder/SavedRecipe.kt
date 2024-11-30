//package com.example.recipefinder
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import android.widget.Toast
//import androidx.fragment.app.Fragment
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.example.recipefinder.entities.Recipe
//
//class SavedRecipesFragment : Fragment() {
//
//    private lateinit var savedRecipesRecyclerView: RecyclerView
//    private lateinit var savedRecipesAdapter: RecipeAdapter
//    private val savedRecipes = mutableListOf<Recipe>()
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.fragment_saved_recipe, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        // Initialize RecyclerView
//        savedRecipesRecyclerView = view.findViewById(R.id.rvSavedRecipes)
//        savedRecipesAdapter = RecipeAdapter(savedRecipes) { recipe ->
//            onRecipeClicked(recipe)
//        }
//        savedRecipesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
//        savedRecipesRecyclerView.adapter = savedRecipesAdapter
//
//        // Load Saved Recipes
//        loadSavedRecipes()
//    }
//
//    private fun loadSavedRecipes() {
//        // Replace with real data loading logic
//        // For example, load from Firestore or local database
//        if (savedRecipes.isEmpty()) {
//            view?.findViewById<TextView>(R.id.tvNoSavedRecipes)?.visibility = View.VISIBLE
//        } else {
//            view?.findViewById<TextView>(R.id.tvNoSavedRecipes)?.visibility = View.GONE
//        }
//    }
//
//    private fun onRecipeClicked(recipe: Recipe) {
//        Toast.makeText(requireContext(), "Clicked: ${recipe.name}", Toast.LENGTH_SHORT).show()
//        // Navigate to Recipe Details or perform another action
//    }
//}