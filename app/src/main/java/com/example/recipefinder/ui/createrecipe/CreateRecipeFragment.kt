package com.example.recipefinder.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.recipefinder.databinding.FragmentCreateRecipeBinding
import com.example.recipefinder.entities.Recipe
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

class CreateRecipeFragment : Fragment() {
    private var _binding: FragmentCreateRecipeBinding? = null
    private val binding get() = _binding!!

    private val ingredients = mutableListOf<String>()
    private val steps = mutableListOf<String>()

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
        updatePreview() // Initialize empty previews
    }

    private fun setupClickListeners() {
        binding.btnAddIngredient.setOnClickListener {
            val ingredient = binding.etIngredient.text.toString().trim()
            if (ingredient.isNotEmpty()) {
                ingredients.add(ingredient)
                binding.etIngredient.text.clear()
                updatePreview()
                showToast("Ingredient added: $ingredient")
            } else {
                showToast("Please enter an ingredient")
            }
        }

        binding.btnAddStep.setOnClickListener {
            val step = binding.etStep.text.toString().trim()
            if (step.isNotEmpty()) {
                steps.add(step)
                binding.etStep.text.clear()
                updatePreview()
                showToast("Step added: $step")
            } else {
                showToast("Please enter a step")
            }
        }

        binding.btnSaveRecipe.setOnClickListener {
            saveRecipe()
        }
    }

    private fun saveRecipe() {
        val recipeName = binding.etRecipeName.text.toString().trim()

        if (recipeName.isEmpty()) {
            showToast("Please enter a recipe name")
            return
        }

        if (ingredients.isEmpty()) {
            showToast("Please add at least one ingredient")
            return
        }

        if (steps.isEmpty()) {
            showToast("Please add at least one step")
            return
        }

        // Show loading state
        setLoadingState(true)

        val recipe = Recipe(
            id = UUID.randomUUID().toString(),
            name = recipeName,
            ingredients = ingredients.toList(),
            steps = steps.toList(),
            userId = auth.currentUser?.uid ?: "",
            imageUri = null,
            vegan = false,
            vegetarian = false
        )

        saveToFirestore(recipe)
    }

    private fun saveToFirestore(recipe: Recipe) {
        db.collection("recipes")
            .document(recipe.id)
            .set(recipe)
            .addOnSuccessListener {
                showToast("Recipe saved successfully!")
                setLoadingState(false)
                // Navigate back to recipe list
                findNavController().popBackStack()
            }
            .addOnFailureListener { e ->
                showToast("Error saving recipe: ${e.message}")
                setLoadingState(false)
            }
    }

    private fun updatePreview() {
        val ingredientsList = ingredients.joinToString("\n") { "• $it" }
        val stepsList = steps.mapIndexed { index, step ->
            "${index + 1}. $step"
        }.joinToString("\n")

        binding.tvIngredientsPreview.text = ingredientsList.ifEmpty { "No ingredients added yet" }
        binding.tvStepsPreview.text = stepsList.ifEmpty { "No steps added yet" }
    }

    private fun setLoadingState(isLoading: Boolean) {
        binding.apply {
//            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            btnSaveRecipe.isEnabled = !isLoading
            btnAddIngredient.isEnabled = !isLoading
            btnAddStep.isEnabled = !isLoading
            etRecipeName.isEnabled = !isLoading
            etIngredient.isEnabled = !isLoading
            etStep.isEnabled = !isLoading
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}