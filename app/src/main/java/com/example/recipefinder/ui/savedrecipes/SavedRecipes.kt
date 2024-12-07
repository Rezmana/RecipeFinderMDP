package com.example.recipefinder.ui.savedrecipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipefinder.R
import com.example.recipefinder.RecipeAdapter
import com.example.recipefinder.entities.Recipe

class SavedRecipes : Fragment() {

    private lateinit var viewModel: SavedRecipesViewModel
    private lateinit var recipeAdapter: RecipeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_saved_recipe, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            SavedRecipesViewModelFactory(requireContext())
        )[SavedRecipesViewModel::class.java]

        // Initialize RecyclerView and Adapter
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvSavedRecipes)

        recipeAdapter = RecipeAdapter(
            recipes = listOf(), // Initialize with an empty list
            isManagingRecipes = false, // Disable delete button for saved recipes
            onDeleteClick = {}, // No delete functionality in this context
            onItemClick = { recipe -> onRecipeClicked(recipe) } // Handle item clicks
        )

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = recipeAdapter

        // Observe ViewModel
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.savedRecipes.observe(viewLifecycleOwner) { recipes ->
            recipeAdapter.updateRecipes(recipes)
        }

        viewModel.toastMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                viewModel.clearToastMessage()
            }
        }
    }

    private fun onRecipeClicked(recipe: Recipe) {
        Toast.makeText(requireContext(), "Clicked: ${recipe.recipeName}", Toast.LENGTH_SHORT).show()
        // Handle navigation or recipe details here
    }
}
