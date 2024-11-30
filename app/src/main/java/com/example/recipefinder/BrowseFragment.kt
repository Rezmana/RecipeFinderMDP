package com.example.recipefinder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipefinder.entities.Recipe

class BrowseFragment : Fragment() {

    private lateinit var backIcon: ImageView
    private lateinit var searchBar: EditText
    private lateinit var searchIcon: ImageView
    private lateinit var cuisineTagsContainer: LinearLayout
    private lateinit var browseDisplayRecipes: RecyclerView
    private lateinit var recipeAdapter: RecipeAdapter

    private val sampleTags = listOf("German", "Japanese", "Middle Eastern", "Chinese", "Italian")
    private val sampleRecipes = listOf(
        Recipe("Dish #1", "https://via.placeholder.com/150"),
        Recipe("Dish #2", "https://via.placeholder.com/150"),
        Recipe("Dish #3", "https://via.placeholder.com/150"),
        Recipe("Dish #4", "https://via.placeholder.com/150"),
        Recipe("Dish #5", "https://via.placeholder.com/150")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_browse, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        backIcon = view.findViewById(R.id.backIcon)
        searchBar = view.findViewById(R.id.searchBar)
        searchIcon = view.findViewById(R.id.searchIcon)
        cuisineTagsContainer = view.findViewById(R.id.cuisineTypeTags)
        browseDisplayRecipes = view.findViewById(R.id.browse_display_recipes)

        // Set up listeners and RecyclerView
        setupListeners()
        setupCuisineTags()
        setupRecipesRecyclerView()
    }

    private fun setupListeners() {
        backIcon.setOnClickListener {
            Toast.makeText(requireContext(), "Back clicked!", Toast.LENGTH_SHORT).show()
            // Navigate back or handle the back action
        }

        searchIcon.setOnClickListener {
            val query = searchBar.text.toString().trim()
            Toast.makeText(requireContext(), "Searching for: $query", Toast.LENGTH_SHORT).show()
            // Implement search functionality if needed
        }
    }

    private fun setupCuisineTags() {
        for (tag in sampleTags) {
            val tagView = TextView(requireContext()).apply {
                text = tag
                textSize = 14f
                setPadding(16, 8, 16, 8)
                setBackgroundResource(R.drawable.tag_background) // Use a drawable for rounded background
                setTextColor(requireContext().getColor(android.R.color.white))
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    marginEnd = 16
                }
                setOnClickListener {
                    onTagClicked(tag)
                }
            }
            cuisineTagsContainer.addView(tagView)
        }
    }

    private fun setupRecipesRecyclerView() {
        recipeAdapter = RecipeAdapter { recipe ->
            onRecipeClicked(recipe)
        }
        browseDisplayRecipes.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        browseDisplayRecipes.adapter = recipeAdapter

        // Populate the adapter with sample data
        recipeAdapter.updateRecipes(sampleRecipes)
    }

    private fun onTagClicked(tag: String) {
        Toast.makeText(requireContext(), "Tag clicked: $tag", Toast.LENGTH_SHORT).show()
        // Filter recipes based on the selected tag and update the adapter
        val filteredRecipes = sampleRecipes.filter { it.recipeName.contains(tag, ignoreCase = true) }
        recipeAdapter.updateRecipes(filteredRecipes)
    }

    private fun onRecipeClicked(recipe: Recipe) {
        Toast.makeText(requireContext(), "Recipe clicked: ${recipe.recipeName}", Toast.LENGTH_SHORT).show()
        // Navigate to recipe details or handle the click action
    }
}
