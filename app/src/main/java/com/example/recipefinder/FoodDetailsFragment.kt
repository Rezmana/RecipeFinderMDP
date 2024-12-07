package com.example.recipefinder

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipefinder.database.RoomDB
import com.example.recipefinder.entities.Recipe
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import java.util.UUID

class FoodDetailsFragment : Fragment() {

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
    private lateinit var btnSaveRecipe : AppCompatImageButton
    private val commentsList = mutableListOf<Comments>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_view_food, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Initialize views
        foodPicture = view.findViewById(R.id.foodPicture)
        btnSaveRecipe = view.findViewById(R.id.btnSaveRecipe)
        foodName = view.findViewById(R.id.foodName)
        typeCuisine = view.findViewById(R.id.cookingTypes)
        difficulty = view.findViewById(R.id.diet)
        prepTime = view.findViewById(R.id.totalServings) // Reusing the view ID for simplicity
        cookingTime = view.findViewById(R.id.totalServings)
        commentsRecyclerView = view.findViewById(R.id.commentsRecyclerView)



//        veganTag = view.findViewById(R.id.vegan)
//        vegetarianTag = view.findViewById(R.id.vegetarianTag)
        ingredientsList = view.findViewById(R.id.ingredientsList)
//        stepsList = view.findViewById(R.id.stepsList)
        commentInput = view.findViewById(R.id.commentInput)
        addCommentButton = view.findViewById(R.id.addCommentButton)



        // Set up RecyclerView for comments
        commentsAdapter = CommentsAdapter(commentsList)
        commentsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        commentsRecyclerView.adapter = commentsAdapter

        // Load recipe details from arguments
        val recipe: Recipe = args.recipe
        val recipeId: String = recipe.id
//        val recipeId = "5219b9f5-41e6-4d19-8559-8fedfeafe7bd"
        Log.d("FoodDetailsFragment", "Recipe ID: ${recipeId}")
        displayFoodDetails(recipe)
        fetchAndDisplayComments(recipeId) // Load existing comments

        btnSaveRecipe.setOnClickListener {
//            saveRecipeForUser(recipe)
            saveRecipeLocally(recipe)
        }

        // Add Comment Button functionality
        addCommentButton.setOnClickListener {
            val commentText = commentInput.text.toString()
            if (commentText.isNotBlank()) {
                val user = FirebaseAuth.getInstance().currentUser
                val userId = user?.uid
                val recipeId = recipeId // Replace with the current recipe's ID

                if (userId != null) {
                    // Fetch the username from Firestore
                    FirebaseFirestore.getInstance().collection("users")
                        .document(userId)
                        .get()
                        .addOnSuccessListener { document ->
                            val userName = document.getString("username") ?: "Unknown User"

                            // Create the comment object
                            val newComment = Comments(
                                userName = userName,
                                commentText = commentText,
                                timeStamp = System.currentTimeMillis()
                            )

                            // Save the comment to the recipe's comments subcollection
                            FirebaseFirestore.getInstance()
                                .collection("recipes")
                                .document(recipeId)
                                .collection("comments")
                                .add(newComment)
                                .addOnSuccessListener {
                                    Toast.makeText(requireContext(), "Comment added", Toast.LENGTH_SHORT).show()
                                    commentInput.text.clear()

                                    // Update the adapter with the new comment
                                    commentsAdapter.addComment(newComment)
                                    commentsRecyclerView.scrollToPosition(commentsAdapter.itemCount - 1)
                                }
                                .addOnFailureListener { exception ->
                                    Toast.makeText(requireContext(), "Failed to add comment: ${exception.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(requireContext(), "Failed to fetch username: ${exception.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
                }
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

    private fun saveRecipeForUser(recipe: Recipe) {
        // Get current user's ID
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            Toast.makeText(context, "You need to log in to save recipes", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = user.uid
        val recipeId = recipe.id.ifEmpty { UUID.randomUUID().toString() }
        Log.d("FirestoreDebug", "Saving recipe with path: users/$userId/savedRecipes/$recipeId")
        // Reference to Firestore
        val firestore = FirebaseFirestore.getInstance()

        // Save recipe under user's savedRecipes subcollection
//        val userRef = firestore.collection("users").document(userId)
//        val savedRecipeRef = userRef.collection("savedRecipes").document(recipe.id)
        val savedRecipeRef = firestore
            .collection("users")
            .document(userId)
            .collection("savedRecipes")
            .document(recipeId)

        savedRecipeRef.set(recipe)
            .addOnSuccessListener {
                saveRecipeLocally(recipe)
                Toast.makeText(context, "Recipe saved successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Failed to save recipe: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveRecipeLocally(recipe: Recipe) {
        val database = RoomDB.getDatabase(requireContext())
        val recipeDao = database.recipeDao()

        // Save the recipe locally in a coroutine
        lifecycleScope.launch {
            recipeDao.saveRecipe(recipe)
            Toast.makeText(requireContext(), "Recipe saved locally!", Toast.LENGTH_SHORT).show()
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



//    private fun fetchAndDisplayComments(recipeId: String) {
//        Log.d("FetchComments", "Fetching comments for Recipe ID: $recipeId")
//        FirebaseFirestore.getInstance()
//            .collection("recipes")
//            .document(recipeId)
//            .collection("comments")
//            .orderBy("timestamp")
//            .get()
//            .addOnSuccessListener { querySnapshot ->
//                Log.d("FetchComments", "Raw document count: ${querySnapshot.size()}")
//                querySnapshot.documents.forEach { document ->
//                    Log.d("FetchComments", "Document ID: ${document.id}, Data: ${document.data}")
//                }
//
//                val comments = querySnapshot.toObjects(Comments::class.java)
//                Log.d("FetchComments", "Parsed ${comments.size} comments")
//                commentsAdapter.updateComments(comments) // Update RecyclerView with comments
//            }
//            .addOnFailureListener { exception ->
//                Log.e("FetchComments", "Error fetching comments: ${exception.message}")
//                Toast.makeText(requireContext(), "Failed to load comments: ${exception.message}", Toast.LENGTH_SHORT).show()
//            }
//    }
private fun fetchAndDisplayComments(recipeId: String) {
    FirebaseFirestore.getInstance()
        .collection("recipes")
        .document(recipeId)
        .collection("comments")
        .orderBy("timeStamp")
        .addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Log.e("FetchComments", "Error: ${exception.message}")
                return@addSnapshotListener
            }

            if (snapshot != null && !snapshot.isEmpty) {
                Log.d("FetchComments", "Documents retrieved: ${snapshot.size()}")
                snapshot.documents.forEach { document ->
                    Log.d("FetchComments", "Document ID: ${document.id}, Data: ${document.data}")
                }

                val comments = snapshot.toObjects(Comments::class.java)
                Log.d("FetchComments", "Parsed ${comments.size} comments")
                commentsAdapter.updateComments(comments)
            } else {
                Log.d("FetchComments", "No comments found for Recipe ID: $recipeId")
            }
        }
}


    //    private fun fetchComments() {
//        FirebaseFirestore.getInstance().collection("comments")
//            .orderBy("timestamp")
//            .get()
//            .addOnSuccessListener { querySnapshot ->
//                val comments = querySnapshot.toObjects(Comments::class.java)
//                commentsAdapter.updateComments(comments) // Update the adapter with the fetched comments
//            }
//            .addOnFailureListener { exception ->
//                Toast.makeText(requireContext(), "Failed to load comments: ${exception.message}", Toast.LENGTH_SHORT).show()
//            }
//    }
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
