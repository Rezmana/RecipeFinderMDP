package com.example.recipefinder.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipefinder.R
import com.example.recipefinder.RecipeAdapter
import com.example.recipefinder.entities.Recipe
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    private lateinit var profilePicture: ImageView
    private lateinit var profileName: TextView
    private lateinit var profileEmail: TextView
    private lateinit var savedRecipesRecyclerView: RecyclerView
    private lateinit var settingsButton: Button

    private lateinit var recipeAdapter: RecipeAdapter
    private val savedRecipes = mutableListOf<Recipe>()
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        profilePicture = view.findViewById(R.id.profilePicture)
        profileName = view.findViewById(R.id.profileName)
        profileEmail = view.findViewById(R.id.profileEmail)
        savedRecipesRecyclerView = view.findViewById(R.id.savedRecipesRecyclerView)
        settingsButton = view.findViewById(R.id.settingsButton)
//TODO:: Don't forget to comment this out and add features to the profile fragment
    }
}

        // Set up RecyclerView
//        recipeAdapter = RecipeAdapter(savedRecipes) { recipe ->
//            onRecipeClicked(recipe)
//        }
//        savedRecipesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
//        savedRecipesRecyclerView.adapter = recipeAdapter
//
//        // Fetch user data and saved recipes
//        fetchUserData()
//        fetchSavedRecipes()
//
//        // Handle settings button click
//        settingsButton.setOnClickListener {
//            openSettings()
//        }
//    }
//
//    private fun fetchUserData() {
//        val currentUser = auth.currentUser
//        if (currentUser != null) {
//            profileName.text = currentUser.displayName ?: "User Name"
//            profileEmail.text = currentUser.email ?: "Email Not Available"
//
//            // You can also load the profile picture with a library like Picasso or Glide
//            // Example:
//            // Picasso.get().load(currentUser.photoUrl).into(profilePicture)
//        } else {
//            profileName.text = "Guest"
//            profileEmail.text = "Not Signed In"
//        }
//    }
//
//    private fun fetchSavedRecipes() {
//        firestore.collection("users")
//            .document(auth.uid ?: return)
//            .collection("savedRecipes")
//            .get()
//            .addOnSuccessListener { querySnapshot ->
//                val recipes = querySnapshot.toObjects(Recipe::class.java)
//                savedRecipes.clear()
//                savedRecipes.addAll(recipes)
//                recipeAdapter.notifyDataSetChanged()
//            }
//            .addOnFailureListener { exception ->
//                Toast.makeText(requireContext(), "Failed to load saved recipes: ${exception.message}", Toast.LENGTH_SHORT).show()
//            }
//    }
//
//    private fun onRecipeClicked(recipe: Recipe) {
//        Toast.makeText(requireContext(), "Clicked on: ${recipe.name}", Toast.LENGTH_SHORT).show()
//        // Navigate to recipe details or perform another action
//    }
//
//    private fun openSettings() {
//        // Navigate to settings screen or open a settings activity
//        Toast.makeText(requireContext(), "Opening Settings", Toast.LENGTH_SHORT).show()
//    }
//
//    companion object {
//        fun newInstance() = ProfileFragment()
//    }
//}
