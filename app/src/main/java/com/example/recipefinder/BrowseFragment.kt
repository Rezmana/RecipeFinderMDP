package com.example.recipefinder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipefinder.entities.Recipe
import com.google.firebase.firestore.FirebaseFirestore

class BrowseFragment : Fragment() {

    private lateinit var backIcon: ImageView
    private lateinit var searchBar: EditText
    private lateinit var searchIcon: ImageView
    private lateinit var cuisineTagsContainer: LinearLayout
    private lateinit var browseDisplayRecipes: RecyclerView
    private lateinit var recipeAdapter: RecipeAdapter

    private lateinit var ingredientGrid: GridLayout
    private val allIngredients = listOf(
        "Flour", "Sugar", "Eggs", "Milk", "Butter", "Salt",
        "Tomatoes", "Chicken", "Cheese", "Rice", "Beans"
    ) // Replace with your actual ingredient list

    private val sampleTags = listOf("German", "Japanese", "Middle Eastern", "Chinese", "Italian")

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

        ingredientGrid = view.findViewById(R.id.ingredientGrid)

//        val recyclerView = view.findViewById<RecyclerView>(R.id.browse_display_recipes)
        recipeAdapter = RecipeAdapter { recipe ->
            Toast.makeText(context, "Clicked: ${recipe.recipeName}", Toast.LENGTH_SHORT).show()
        }
        browseDisplayRecipes.layoutManager = LinearLayoutManager(context)
        browseDisplayRecipes.adapter = recipeAdapter

        // Set up listeners and RecyclerView
        setupIngredientGrid()
        setupListeners()
        setupCuisineTags()
        setupRecipesRecyclerView()
    }

    private fun setupListeners() {
        backIcon.setOnClickListener {
            Toast.makeText(requireContext(), "Back clicked!", Toast.LENGTH_SHORT).show()
            // Navigate back or handle the back action
            findNavController().popBackStack() // Navigate back in the NavController stack
        }

        searchIcon.setOnClickListener {
            val query = searchBar.text.toString().trim()
            Toast.makeText(requireContext(), "Searching for: $query", Toast.LENGTH_SHORT).show()
            // Implement search functionality if needed

        }
    }

    private fun fetchAllRecipes() {
        val firestore = FirebaseFirestore.getInstance()

        firestore.collection("recipes")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val recipeList = querySnapshot.toObjects(Recipe::class.java)
                recipeAdapter.updateRecipes(recipeList)
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Failed to fetch recipes: ${exception.message}", Toast.LENGTH_SHORT).show()
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
            onRecipeClicked(recipe) // Handle clicks if needed
        }

        browseDisplayRecipes.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        browseDisplayRecipes.adapter = recipeAdapter

        // Fetch all recipes and populate the adapter
        fetchAllRecipes()
    }

    private fun onTagClicked(tag: String) {
        Toast.makeText(requireContext(), "Tag clicked: $tag", Toast.LENGTH_SHORT).show()
        // Filter recipes based on the selected tag and update the adapter
//        val filteredRecipes = sampleRecipes.filter { it.recipeName.contains(tag, ignoreCase = true) }
//        recipeAdapter.updateRecipes(filteredRecipes)
    }

    private fun setupIngredientGrid() {
        for (ingredient in allIngredients) {
            val button = Button(requireContext()).apply {
                text = ingredient
                layoutParams = GridLayout.LayoutParams().apply {
                    width = 0
                    height = GridLayout.LayoutParams.WRAP_CONTENT
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                    rowSpec = GridLayout.spec(GridLayout.UNDEFINED)
                    marginEnd = 8
                    bottomMargin = 8
                }
                setOnClickListener {
                    filterRecipesByIngredient(ingredient) // Call the Firestore filter function
                }
            }
            ingredientGrid.addView(button)
        }
    }

    private fun populateIngredientGrid() {
        // Loop through the ingredient list and create buttons dynamically
        for (ingredient in allIngredients) {
            val button = Button(requireContext()).apply {
                text = ingredient
                layoutParams = GridLayout.LayoutParams().apply {
                    width = 0 // Match parent
                    height = GridLayout.LayoutParams.WRAP_CONTENT
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                    marginEnd = 8
                    bottomMargin = 8
                }

                // Set click listener for each button
                setOnClickListener {
                    filterRecipesByIngredient(ingredient)
                }
            }

            // Add the button to the GridLayout
            ingredientGrid.addView(button)
        }
    }

    private fun filterRecipesByIngredient(ingredient: String) {
        val firestore = FirebaseFirestore.getInstance()

        // Query Firestore for recipes containing the selected ingredient
        firestore.collection("recipes")
            .whereArrayContains("ingredients", ingredient)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val filteredRecipes = querySnapshot.toObjects(Recipe::class.java)
                recipeAdapter.updateRecipes(filteredRecipes)

                Toast.makeText(context, "Filtered by: $ingredient", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Failed to load recipes: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun onRecipeClicked(recipe: Recipe) {
        Toast.makeText(requireContext(), "Recipe clicked: ${recipe.recipeName}", Toast.LENGTH_SHORT).show()
        // Navigate to recipe details or handle the click action
    }
}
