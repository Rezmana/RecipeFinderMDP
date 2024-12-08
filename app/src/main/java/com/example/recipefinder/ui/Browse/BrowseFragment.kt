package com.example.recipefinder.ui.Browse

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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipefinder.R
import com.example.recipefinder.RecipeAdapter
import com.example.recipefinder.entities.Recipe
import com.example.recipefinder.ui.Browse.BrowseViewModel

class BrowseFragment : Fragment() {

    private lateinit var viewModel: BrowseViewModel
    private lateinit var recipeAdapter: RecipeAdapter

    private lateinit var searchBar: EditText
    private lateinit var searchIcon: ImageView
    private lateinit var cuisineTagsContainer: LinearLayout
    private lateinit var browseDisplayRecipes: RecyclerView
    private lateinit var ingredientGrid: GridLayout
    private lateinit var clearFilterButton: Button


    private val sampleTags = listOf("Italian", "Mexican", "Indian", "Chinese", "French")
    private val allIngredients = listOf(
        "Flour", "Sugar", "Eggs", "Milk", "Butter", "Salt",
        "Tomatoes", "Chicken", "Cheese", "Rice", "Beans"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_browse, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this)[BrowseViewModel::class.java]

        // Initialize views
        searchBar = view.findViewById(R.id.searchBar)
        searchIcon = view.findViewById(R.id.searchIcon)
        cuisineTagsContainer = view.findViewById(R.id.cuisineTypeTags)
        browseDisplayRecipes = view.findViewById(R.id.browse_display_recipes)
        ingredientGrid = view.findViewById(R.id.ingredientGrid)
        clearFilterButton = view.findViewById(R.id.clearFilterButton)

        // Setup RecyclerView
        setupRecyclerView()

        // Set up UI components
        setupIngredientGrid()
        setupCuisineTags()
        setupListeners()

        clearFilterButton.setOnClickListener {
            viewModel.fetchAllRecipes() // Reset to original list
        }

        // Observe ViewModel
        observeViewModel()
    }

    private fun setupRecyclerView() {
        recipeAdapter = RecipeAdapter(
            recipes = listOf(), // Initialize with an empty list
            isManagingRecipes = false, // No delete button for browsing
            onDeleteClick = {}, // No delete action needed here
            onItemClick = { recipe -> onRecipeClicked(recipe) } // Handle item clicks
        )

        browseDisplayRecipes.layoutManager = LinearLayoutManager(requireContext())
        browseDisplayRecipes.adapter = recipeAdapter
    }

    private fun observeViewModel() {
        viewModel.recipes.observe(viewLifecycleOwner) { recipes ->
            recipeAdapter.updateRecipes(recipes) // Update adapter when recipes change
        }


        viewModel.toastMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                viewModel.clearToastMessage()
            }
        }
    }

    private fun setupListeners() {
        searchIcon.setOnClickListener {
            val query = searchBar.text.toString().trim()
            if (query.isNotEmpty()) {
                viewModel.searchRecipes(query) // Trigger search in ViewModel
            } else {
                Toast.makeText(requireContext(), "Please enter a search query", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupIngredientGrid() {
        for (ingredient in allIngredients) {
            val button = Button(requireContext()).apply {
                text = ingredient
                layoutParams = GridLayout.LayoutParams().apply {
                    width = 0
                    height = GridLayout.LayoutParams.WRAP_CONTENT
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                    marginEnd = 8
                    bottomMargin = 8
                }
                setOnClickListener {
                    viewModel.filterRecipesByIngredient(ingredient) // Trigger filter in ViewModel
                }
            }
            ingredientGrid.addView(button)
        }
    }

    private fun setupCuisineTags() {
        for (tag in sampleTags) {
            val tagView = TextView(requireContext()).apply {
                text = tag
                textSize = 14f
                setPadding(16, 8, 16, 8)
                setBackgroundResource(R.drawable.tag_background)
                setTextColor(requireContext().getColor(android.R.color.white))
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    marginEnd = 16
                }
                setOnClickListener {
                    viewModel.filterRecipesByCuisineType(tag) // Add tag-based filter in ViewModel
                }
            }
            cuisineTagsContainer.addView(tagView)
        }
    }

    private fun onRecipeClicked(recipe: Recipe) {
        Toast.makeText(requireContext(), "Recipe clicked: ${recipe.recipeName}", Toast.LENGTH_SHORT).show()
        // Navigate to recipe details or handle click action
        val action = BrowseFragmentDirections.actionNavigationBrowseToFoodDetailsFragment(recipe)
        findNavController().navigate(action)
    }
}
