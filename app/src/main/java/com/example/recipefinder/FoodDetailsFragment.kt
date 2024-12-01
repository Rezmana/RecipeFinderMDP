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

class FoodDetailsFragment : DialogFragment() {

    private val args: FoodDetailsFragmentArgs by navArgs()

    private lateinit var foodPicture: ImageView
    private lateinit var foodName: TextView
    private lateinit var typeCuisine: TextView
    private lateinit var difficulty: TextView
    private lateinit var prepTime: TextView
    private lateinit var cookingTime: TextView
    private lateinit var veganTag: TextView
    private lateinit var vegetarianTag: TextView
    private lateinit var ingredientsList: LinearLayout
    private lateinit var stepsList: TextView
    private lateinit var commentInput: EditText
    private lateinit var addCommentButton: Button
    private lateinit var commentsRecyclerView: RecyclerView
    private lateinit var commentsAdapter: CommentsAdapter
    private val commentsList = mutableListOf<Comments>()

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
        foodName = view.findViewById(R.id.foodName)
        typeCuisine = view.findViewById(R.id.cookingTypes)
//        difficulty = view.findViewById(R.id.diet)
//        prepTime = view.findViewById(R.id.totalServings) // Reusing the view ID for simplicity
//        cookingTime = view.findViewById(R.id.totalServings)
//        veganTag = view.findViewById(R.id.vegan)
//        vegetarianTag = view.findViewById(R.id.vegetarianTag)
        ingredientsList = view.findViewById(R.id.ingredientsList)
//        stepsList = view.findViewById(R.id.stepsList)
        commentInput = view.findViewById(R.id.commentInput)
        addCommentButton = view.findViewById(R.id.addCommentButton)
        commentsRecyclerView = view.findViewById(R.id.commentsRecyclerView)

        // Set up RecyclerView for comments
        commentsAdapter = CommentsAdapter(commentsList)
        commentsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        commentsRecyclerView.adapter = commentsAdapter

        // Load recipe details from arguments
        val recipe: Recipe = args.recipe
        displayFoodDetails(recipe)

        // Add Comment Button functionality
        addCommentButton.setOnClickListener {
            val commentText = commentInput.text.toString()
            if (commentText.isNotBlank()) {
                val newComment = Comments(userName = "User", commentText = commentText)
                commentsAdapter.addComment(newComment)
                commentInput.text.clear()
                commentsRecyclerView.scrollToPosition(commentsList.size - 1)
            } else {
                Toast.makeText(requireContext(), "Comment cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        fun newInstance(recipe: Recipe): FoodDetailsFragment {
            val fragment = FoodDetailsFragment()
            val args = Bundle().apply {
                putParcelable("recipe", recipe)
            }
            fragment.arguments = args
            return fragment
        }
    }

    private fun displayFoodDetails(recipe: Recipe) {
        foodName.text = recipe.recipeName
//        typeCuisine.text = "Cuisine: ${recipe.typeCuisine ?: "N/A"}"
        typeCuisine.text = "${recipe.typeCuisine ?: "N/A"}"
        difficulty.text = "Difficulty: ${recipe.difficulty ?: "N/A"}"
        prepTime.text = "Prep Time: ${recipe.prepTime ?: "N/A"}"
        cookingTime.text = "Cooking Time: ${recipe.cookingTime ?: "N/A"}"

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

