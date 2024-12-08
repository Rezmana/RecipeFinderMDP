package com.example.recipefinder.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipefinder.RecipeAdapter
import com.example.recipefinder.R
import com.example.recipefinder.entities.Recipe
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class HomeFragment : Fragment() {
    private lateinit var viewModel: HomeViewModel
    private lateinit var recipeAdapter: RecipeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        val tvGreeting = view.findViewById<TextView>(R.id.tv_greeting)
        val tvCommunity = view.findViewById<TextView>(R.id.tv_community)
        val tvBrowse = view.findViewById<TextView>(R.id.tv_browse)
        val featuredRecyclerView = view.findViewById<RecyclerView>(R.id.rv_featured_recipes)

        // Initialize RecyclerView Adapter
        recipeAdapter = RecipeAdapter(
            recipes = listOf(), // Empty list initially
            isManagingRecipes = false, // Disable delete button
            onDeleteClick = {}, // No delete functionality in HomeFragment
            onItemClick = { recipe -> navigateToFoodDetails(recipe) } // Handle item click
        )


        featuredRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        featuredRecyclerView.adapter = recipeAdapter

        // Observe ViewModel
        ObserveViewModel()

        // Set up UI interactions
        setupClickListeners(tvCommunity, tvBrowse,)

        // Observe ViewModel
//        viewModel.greeting.observe(viewLifecycleOwner) { greeting ->
//            tvGreeting.text = greeting
//        }
//
//        viewModel.featuredRecipes.observe(viewLifecycleOwner) { recipes ->
//            recipeAdapter.updateRecipes(recipes)
//        }
//
//        viewModel.toastMessage.observe(viewLifecycleOwner) { message ->
//            message?.let {
//                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
//                viewModel.clearToastMessage()
//            }
//        }
//
//        // Click listeners
//        tvCommunity.setOnClickListener {
//            // Placeholder for Community navigation
//        }
//
//        tvBrowse.setOnClickListener {
//            findNavController().navigate(R.id.action_navigation_home_to_browseFragment)
//        }
//
//        tvSeeMoreFeatured.setOnClickListener {
//            // Placeholder for Featured section navigation
//        }
//
//        tvSeeMoreRecommendation.setOnClickListener {
//            // Placeholder for Recommendations navigation
//        }
    }

    private fun ObserveViewModel() {
        viewModel.featuredRecipes.observe(viewLifecycleOwner) { recipes ->
            recipeAdapter.updateRecipes(recipes) // Update adapter with new recipes
        }

        viewModel.greeting.observe(viewLifecycleOwner) { greeting ->
            view?.findViewById<TextView>(R.id.tv_greeting)?.text = greeting
        }
    }

    private fun setupClickListeners(
        tvCommunity: TextView,
        tvBrowse: TextView,
    ) {
        tvCommunity.setOnClickListener {
            Toast.makeText(requireContext(), "Community clicked", Toast.LENGTH_SHORT).show()
        }

        tvBrowse.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_browseFragment)
        }

    }


    private fun navigateToFoodDetails(recipe: Recipe) {
//        val action =
        val action = HomeFragmentDirections.actionNavigationHomeToFoodDetailsFragment(recipe)
        findNavController().navigate(action)
    }
}

