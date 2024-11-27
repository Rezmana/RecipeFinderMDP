package com.example.recipefinder.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipefinder.FeaturedRecipeAdapter
import com.example.recipefinder.R
import com.example.recipefinder.entities.Recipe
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class HomeFragment : Fragment() {
    private lateinit var tvGreeting: TextView
    private lateinit var tvCommunity: TextView
    private lateinit var tvBrowse: TextView
    private lateinit var tvFeatured: TextView
    private lateinit var tvSeeMoreFeatured: TextView
    private lateinit var tvRecommendation: TextView
    private lateinit var tvSeeMoreRecommendation: TextView
    private lateinit var featuredRecyclerView: RecyclerView
    private lateinit var featuredRecipeAdapter: FeaturedRecipeAdapter
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
        tvCommunity = view.findViewById(R.id.tv_community)
        tvBrowse = view.findViewById(R.id.tv_browse)
        tvFeatured = view.findViewById(R.id.tv_featured)
        tvSeeMoreFeatured = view.findViewById(R.id.tv_see_more_featured)
        tvRecommendation = view.findViewById(R.id.tv_recommendation)
        tvSeeMoreRecommendation = view.findViewById(R.id.tv_see_more_recommendation)

        featuredRecyclerView = view.findViewById(R.id.rv_featured_recipes)
        featuredRecipeAdapter = FeaturedRecipeAdapter { recipe ->
            navigateToFoodDetails(recipe)
        }

        featuredRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = featuredRecipeAdapter
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
            //TODO:UNCOmment this out
//            .limit(5)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val recipes = querySnapshot.toObjects(Recipe::class.java)
                featuredRecipeAdapter.updateRecipes(recipes)
            }
            .addOnFailureListener { exception ->
                // Handle error
                Toast.makeText(context, "Failed to load recipes: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun setupClickListeners() {
        // Example navigation or action listeners
        tvCommunity.setOnClickListener {
            // Navigate to Community fragment
            // findNavController().navigate(R.id.action_homeFragment_to_communityFragment)
        }

        tvBrowse.setOnClickListener {
            // Navigate to Browse fragment
            // findNavController().navigate(R.id.action_homeFragment_to_browseFragment)
        }

        tvSeeMoreFeatured.setOnClickListener {
            // Navigate to Featured section
            // findNavController().navigate(R.id.action_homeFragment_to_featuredFragment)
        }

        tvSeeMoreRecommendation.setOnClickListener {
            // Navigate to Recommendations
            // findNavController().navigate(R.id.action_homeFragment_to_recommendationsFragment)
        }
    }

    private fun updateGreeting() {
        // You can customize this to show user-specific greeting
        val currentTime = System.currentTimeMillis()
        val greeting = when (getCurrentTimeOfDay()) {
            "morning" -> "Good Morning!"
            "afternoon" -> "Good Afternoon!"
            "evening" -> "Good Evening!"
            else -> "Hello User!"
        }
        tvGreeting.text = greeting
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
        val action = HomeFragmentDirections.actionNavigationHomeToFoodDetailsFragment(recipe)
        findNavController().navigate(action)
    }

    companion object {
        fun newInstance() = HomeFragment()
    }
}
