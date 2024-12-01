package com.example.recipefinder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipefinder.entities.Recipe
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class FoodDialogDetailsFragment : DialogFragment() {

    private val args: FoodDialogDetailsFragmentArgs by navArgs()

    private lateinit var foodPicture: ImageView
    private lateinit var userName : TextView
    private lateinit var foodName: TextView
    private lateinit var recipeDescription : TextView
    private lateinit var typeCuisine: TextView
    private lateinit var ingredientsList: LinearLayout
    private lateinit var btnSaveRecipe : AppCompatImageButton

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
        recipeDescription = view.findViewById(R.id.etDialogFoodDescription)
        foodName = view.findViewById(R.id.foodName)
        typeCuisine = view.findViewById(R.id.cookingTypes)
        userName = view.findViewById(R.id.author)
        ingredientsList = view.findViewById(R.id.ingredientsList)
        btnSaveRecipe = view.findViewById(R.id.btnSaveRecipe)

        // Set up RecyclerView for comments

        // Load recipe details from arguments
        val recipe: Recipe = args.recipe
        displayFoodDetails(recipe)

        btnSaveRecipe.setOnClickListener {
            saveRecipeForUser(recipe)
        }

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
        typeCuisine.text = "${recipe.typeCuisine ?: "N/A"}"
//

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
    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.85).toInt(),
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setBackgroundDrawableResource(android.R.color.white)
    }
    private fun saveRecipeForUser(recipe: Recipe) {
        // Get current user's ID
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            Toast.makeText(context, "You need to log in to save recipes", Toast.LENGTH_SHORT).show()
            return
        }

        // Reference to Firestore
        val firestore = FirebaseFirestore.getInstance()

        // Save recipe under user's savedRecipes subcollection
        val userRef = firestore.collection("users").document(userId)
        val savedRecipeRef = userRef.collection("savedRecipes").document(recipe.id)

        savedRecipeRef.set(recipe)
            .addOnSuccessListener {
                Toast.makeText(context, "Recipe saved successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Failed to save recipe: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}

