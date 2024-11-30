package com.example.recipefinder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipefinder.entities.Recipe
import com.squareup.picasso.Picasso

class FoodDialogDetailsFragment : DialogFragment() {

    private val args: FoodDialogDetailsFragmentArgs by navArgs()

    private lateinit var foodPicture: ImageView
    private lateinit var userName : TextView
    private lateinit var foodName: TextView
    private lateinit var recipeDescription : TextView
    private lateinit var typeCuisine: TextView
    private lateinit var difficulty: TextView
    private lateinit var prepTime: TextView
    private lateinit var cookingTime: TextView
    private lateinit var veganTag: TextView
    private lateinit var vegetarianTag: TextView
    private lateinit var ingredientsList: LinearLayout
    private val commentsList = mutableListOf<Comment>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dialog_food_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views

        foodPicture = view.findViewById(R.id.foodPicture)
        recipeDescription = view.findViewById(R.id.etDialogFoodDescription)
        foodName = view.findViewById(R.id.foodName)
        typeCuisine = view.findViewById(R.id.cookingTypes)
        userName = view.findViewById(R.id.author)
//        difficulty = view.findViewById(R.id.diet)
//        prepTime = view.findViewById(R.id.totalServings) // Reusing the view ID for simplicity
//        cookingTime = view.findViewById(R.id.totalServings)
//        veganTag = view.findViewById(R.id.vegan)
//        vegetarianTag = view.findViewById(R.id.vegetarianTag)
        ingredientsList = view.findViewById(R.id.ingredientsList)
//        stepsList = view.findViewById(R.id.stepsList)

        // Set up RecyclerView for comments

        // Load recipe details from arguments
        val recipe: Recipe = args.recipe
        displayFoodDetails(recipe)

        // Add Comment Button functionality
    }

    companion object {
        fun newInstance(recipe: Recipe): FoodDialogDetailsFragment {
            val fragment = FoodDialogDetailsFragment()
            val args = Bundle().apply {
                putParcelable("recipe", recipe)
            }
            fragment.arguments = args
            return fragment
        }
    }

    private fun displayFoodDetails(recipe: Recipe) {
        foodName.text = recipe.recipeName
        userName.text = recipe.userName
        recipeDescription.text = recipe.recipeDescription
//        typeCuisine.text = "Cuisine: ${recipe.typeCuisine ?: "N/A"}"
        typeCuisine.text = "${recipe.typeCuisine ?: "N/A"}"
//        difficulty.text = "Difficulty: ${recipe.difficulty ?: "N/A"}"
//        prepTime.text = "Prep Time: ${recipe.prepTime ?: "N/A"}"
//        cookingTime.text = "Cooking Time: ${recipe.cookingTime ?: "N/A"}"

//        veganTag.visibility = if (recipe.vegan) View.VISIBLE else View.GONE
//        vegetarianTag.visibility = if (recipe.vegetarian) View.VISIBLE else View.GONE

//        ingredientsList.text = "Ingredients:\n" + recipe.ingredients.joinToString(separator = "\n") { "- $it" }
//        stepsList.text = "Steps:\n" + recipe.steps.joinToString(separator = "\n") { "$it" }

        // Load image using Picasso
        recipe.imageUri?.let {
            Picasso.get()
                .load(it)
                .placeholder(R.drawable.background_placeholder)
//                .error(R.drawable.error_image)
                .into(foodPicture)
        } ?: run {
            foodPicture.setImageResource(R.drawable.background_placeholder)
        }

        populateIngredients(recipe.ingredients)
    }

    private fun populateIngredients(ingredients: List<String>) {
        // Clear existing views
        ingredientsList.removeAllViews()

        // Add each ingredient as a TextView
        for (ingredient in ingredients) {
            val ingredientView = TextView(requireContext()).apply {
                text = "- $ingredient"
                textSize = 14f
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    bottomMargin = 8 // Add spacing between items
                }
            }
            ingredientsList.addView(ingredientView)
        }
    }
}

