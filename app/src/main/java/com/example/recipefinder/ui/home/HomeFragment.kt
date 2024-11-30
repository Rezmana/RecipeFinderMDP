package com.example.recipefinder.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipefinder.RecipeAdapter
import com.example.recipefinder.FoodDetailsFragment
import com.example.recipefinder.FoodDialogDetailsFragment
import com.example.recipefinder.R
import com.example.recipefinder.entities.Recipe
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class HomeFragment : Fragment() {
    private lateinit var tvGreeting: TextView
    private lateinit var tvCommunity: TextView
    private lateinit var tvUser: TextView
    private lateinit var tvBrowse: TextView
    private lateinit var tvFeatured: TextView
    private lateinit var tvSeeMoreFeatured: TextView
    private lateinit var tvRecommendation: TextView
    private lateinit var tvSeeMoreRecommendation: TextView
    private lateinit var featuredRecyclerView: RecyclerView
    private lateinit var recipeAdapter: RecipeAdapter
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        tvGreeting = view.findViewById(R.id.tv_greeting)
//        tvUser = view.findViewById(R.id.tv_user)
        tvCommunity = view.findViewById(R.id.tv_community)
        tvBrowse = view.findViewById(R.id.tv_browse)
        tvFeatured = view.findViewById(R.id.tv_featured)
        tvSeeMoreFeatured = view.findViewById(R.id.tv_see_more_featured)
        tvRecommendation = view.findViewById(R.id.tv_recommendation)
        tvSeeMoreRecommendation = view.findViewById(R.id.tv_see_more_recommendation)

        featuredRecyclerView = view.findViewById(R.id.rv_featured_recipes)
        recipeAdapter = RecipeAdapter { recipe ->
            navigateToFoodDetails(recipe)
        }

        featuredRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = recipeAdapter
        }

        // Fetch featured recipes from Firestore
        fetchFeaturedRecipes()
        // Set up click listeners
        setupClickListeners()

        // Customize greeting (optional)
        updateGreeting()
    }

    private fun fetchFeaturedRecipes() {
        firestore.collection("recipes")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val recipes = querySnapshot.toObjects(Recipe::class.java)
                recipeAdapter.updateRecipes(recipes)
            }
            .addOnFailureListener { exception ->
                // Handle error
                Toast.makeText(context, "Failed to load recipes: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun setupClickListeners() {
        tvCommunity.setOnClickListener {
            // Placeholder for navigation to Community fragment
        }

        tvBrowse.setOnClickListener {
            // Placeholder for navigation to Browse fragment
            val dialogFragment = FoodDialogDetailsFragment()
            dialogFragment.show(parentFragmentManager, "FoodDetailsDialogFragment")
        }

        tvSeeMoreFeatured.setOnClickListener {
            // Placeholder for navigation to Featured section
        }

        tvSeeMoreRecommendation.setOnClickListener {
            // Placeholder for navigation to Recommendations
        }
    }

    private fun updateGreeting() {
        val greeting = when (getCurrentTimeOfDay()) {
            "morning" -> "Good Morning"
            "afternoon" -> "Good Afternoon"
            "evening" -> "Good Evening"
            else -> "Hello"
        }

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val userId = user.uid
            // Fetch username from Firestore
            FirebaseFirestore.getInstance().collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener { document ->
                    val username = document.getString("username") ?: "User"
                    tvGreeting.text = "$greeting, $username!"
                }
                .addOnFailureListener {
                    tvGreeting.text = "$greeting, User!" // Fallback
                    Toast.makeText(context, "Failed to fetch username", Toast.LENGTH_SHORT).show()
                }
        } else {
            tvGreeting.text = "$greeting, User!" // Fallback if no user is logged in
        }
    }

    private fun getCurrentTimeOfDay(): String {
        return when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
            in 0..11 -> "morning"
            in 12..16 -> "afternoon"
            in 17..23 -> "evening"
            else -> "day"
        }
    }

    private fun navigateToFoodDetails(recipe: Recipe) {
        val dialogFragment = FoodDialogDetailsFragment.newInstance(recipe)
        dialogFragment.show(parentFragmentManager, "FoodDetailsDialogFragment")
    }

    companion object {
        fun newInstance() = HomeFragment()
    }
}
