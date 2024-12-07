package com.example.recipefinder.ui.managerecipes

import android.app.AlertDialog
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

//class ManageRecipesFragment : Fragment() {
//
//    private lateinit var viewModel: ManageRecipesViewModel
//    private lateinit var recipeAdapter: RecipeAdapter
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        return inflater.inflate(R.layout.fragment_manage_recipes, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        viewModel = ViewModelProvider(this)[ManageRecipesViewModel::class.java]
//
//        val recyclerView = view.findViewById<RecyclerView>(R.id.rvManageRecipes)
//        recipeAdapter = RecipeAdapter { recipe ->
//            onRecipeClicked(recipe)
//        }
//
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        recyclerView.adapter = recipeAdapter
//
//        viewModel.recipes.observe(viewLifecycleOwner) { recipes ->
//            recipeAdapter.updateRecipes(recipes)
//        }
//
//        viewModel.toastMessage.observe(viewLifecycleOwner) { message ->
//            message?.let {
//                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
//                viewModel.clearToastMessage()
//            }
//        }
//
//        viewModel.fetchUserRecipes()
//    }
//
//    private fun onRecipeClicked(recipe: Recipe) {
//        AlertDialog.Builder(requireContext())
//            .setTitle("Delete Recipe")
//            .setMessage("Are you sure you want to delete this recipe?")
//            .setPositiveButton("Delete") { _, _ ->
//                viewModel.deleteUserRecipe(recipe.id)
//            }
//            .setNegativeButton("Cancel", null)
//            .show()
//    }
//}

class ManageRecipesFragment : Fragment() {

    private lateinit var viewModel: ManageRecipesViewModel
    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_manage_recipes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rvManageRecipes)
        viewModel = ViewModelProvider(this)[ManageRecipesViewModel::class.java]

        // Observe the recipe list from the ViewModel
        viewModel.recipes.observe(viewLifecycleOwner) { recipes ->
            setupRecyclerViewForManagingRecipes(recipes)
        }

        // Handle toast messages (optional)
        viewModel.toastMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                viewModel.clearToastMessage() // Clear the message after showing
            }
        }

        // Fetch the recipes created by the user
        viewModel.fetchUserRecipes()
    }

    private fun setupRecyclerViewForManagingRecipes(recipes: List<Recipe>) {
        recipeAdapter = RecipeAdapter(
            recipes = recipes,
            isManagingRecipes = true, // Enable delete button
            onDeleteClick = { recipe ->
                showDeleteConfirmationDialog(recipe) // Handle delete
            },
            onItemClick = { recipe ->
                Toast.makeText(requireContext(), "Recipe clicked: ${recipe.recipeName}", Toast.LENGTH_SHORT).show()
                // Handle navigation if needed
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = recipeAdapter
    }

    private fun showDeleteConfirmationDialog(recipe: Recipe) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Recipe")
            .setMessage("Are you sure you want to delete this recipe?")
            .setPositiveButton("Delete") { _, _ ->
                viewModel.deleteRecipe(recipe) // Delete the recipe from Firestore
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
