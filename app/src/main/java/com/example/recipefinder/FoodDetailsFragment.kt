package com.example.recipefinder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipefinder.Comment
import com.example.recipefinder.CommentsAdapter
import com.example.recipefinder.R
import com.example.recipefinder.entities.Recipe
import com.squareup.picasso.Picasso


class FoodDetailsFragment : Fragment() {

    // Use navigation arguments to safely retrieve the recipe
    private val args: FoodDetailsFragmentArgs by navArgs()

    private lateinit var foodPicture: ImageView
    private lateinit var foodName: TextView
    private lateinit var author: TextView
    private lateinit var cookingTypes: TextView
    private lateinit var diet: TextView
    private lateinit var totalServings: TextView
    private lateinit var commentInput: EditText
    private lateinit var addCommentButton: Button
    private lateinit var commentsRecyclerView: RecyclerView
    private lateinit var commentsAdapter: CommentsAdapter

    // Use a mutable list to manage comments
    private val commentsList = mutableListOf<Comment>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_view_food, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        initializeViews(view)

        // Set up comments RecyclerView
        setupCommentsRecyclerView()

        // Retrieve and display recipe details
        displayFoodDetails()

        // Set up comment addition functionality
        setupCommentAddition()
    }

    private fun initializeViews(view: View) {
        foodPicture = view.findViewById(R.id.foodPicture)
        foodName = view.findViewById(R.id.foodName)
        author = view.findViewById(R.id.author)
        cookingTypes = view.findViewById(R.id.cookingTypes)
        diet = view.findViewById(R.id.diet)
        totalServings = view.findViewById(R.id.totalServings)
        commentInput = view.findViewById(R.id.commentInput)
        addCommentButton = view.findViewById(R.id.addCommentButton)
        commentsRecyclerView = view.findViewById(R.id.commentsRecyclerView)
    }

    private fun setupCommentsRecyclerView() {
        commentsAdapter = CommentsAdapter(commentsList)
        commentsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = commentsAdapter
        }
    }

    private fun displayFoodDetails() {
        // Safely retrieve recipe from navigation arguments
        val recipe = args.recipe

        // Populate UI with recipe details
        with(recipe) {
            foodName.text = name
            author.text = "By: $author"
//            cookingTypes.text = "Type of Cooking: $cookingType"
//            diet.text = "Diet: $dietDetails"
            totalServings.text = "Total Servings: $totalServings pax"

            // Load image with error handling
            Picasso.get()
                .load(imageUri)
                .placeholder(R.drawable.background_placeholder)
//                .error(R.drawable.food_picture_placeholder)
                .into(foodPicture)
        }
    }

    private fun setupCommentAddition() {
        addCommentButton.setOnClickListener {
            val commentText = commentInput.text.toString().trim()

            if (commentText.isNotEmpty()) {
                // Create a new comment
                val newComment = Comment(
                    userName = getCurrentUsername(),
                    commentText = commentText
                )

                // Add comment to the list and update RecyclerView
                commentsList.add(newComment)
                commentsAdapter.notifyItemInserted(commentsList.size - 1)

                // Clear input and scroll to bottom
                commentInput.text.clear()
                commentsRecyclerView.scrollToPosition(commentsList.size - 1)
            } else {
                // Show error for empty comment
                commentInput.error = "Comment cannot be empty"
            }
        }
    }

    // Helper method to get current username (replace with actual logic)
    private fun getCurrentUsername(): String {
        // TODO: Implement actual user authentication
        return "Anonymous User"
    }
}