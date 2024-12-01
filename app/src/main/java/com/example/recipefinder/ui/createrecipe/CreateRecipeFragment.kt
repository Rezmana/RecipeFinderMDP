package com.example.recipefinder.ui.createrecipe

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.recipefinder.R
import com.example.recipefinder.databinding.FragmentCreateRecipeBinding
import com.example.recipefinder.entities.Recipe
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import androidx.lifecycle.lifecycleScope
import com.example.recipefinder.DialogLoadingFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID

class CreateRecipeFragment : Fragment() {
    private var _binding: FragmentCreateRecipeBinding? = null
    private val binding get() = _binding!!
    private lateinit var loadingDialog : DialogLoadingFragment

    private val ingredients = mutableListOf<String>()
    private val steps = mutableListOf<String>()
    private val recipeDescription = String

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private var imageUri: Uri? = null

    // Image Picker
    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            data?.data?.let { uri ->
                try {
                    // Validate the URI
                    val contentResolver = requireContext().contentResolver
                    val mimeType = contentResolver.getType(uri)

                    // Check if the selected file is an image
                    if (mimeType?.startsWith("image/") == true) {
                        imageUri = uri
                        binding.ivRecipeImage.setImageURI(uri)
                    } else {
                        showToast("Please select a valid image file")
                        imageUri = null
                    }
                } catch (e: Exception) {
                    Log.e("ImagePicker", "Error processing image", e)
                    showToast("Error selecting image: ${e.localizedMessage}")
                    imageUri = null
                }
            } ?: run {
                showToast("No image was selected")
                imageUri = null
            }
        }
    }

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
        setupSpinners()
        setupClickListeners()
        updatePreview() // Initialize empty previews
    }

    private fun setupSpinners() {
        // Cuisine Spinner
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.cuisine_types,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerCuisine.adapter = adapter
        }

        // Difficulty Spinner
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.difficulty_levels,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerDifficulty.adapter = adapter
        }
    }

    private fun setupClickListeners() {
        // Image Upload
        binding.btnUploadImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            imagePickerLauncher.launch(intent)
        }

        // Add Ingredient
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

        // Add Step
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

        // Save Recipe
        binding.btnSaveRecipe.setOnClickListener {
            saveRecipe()
        }
    }



    private fun saveRecipe() {
        val recipeName = binding.etRecipeName.text.toString().trim()
        val recipeDescription = binding.etRecipeDescription.text.toString().trim()

        // Validation checks
        if (recipeName.isEmpty()) {
            showToast("Please enter a recipe name")
            return
        }

        if (ingredients.isEmpty()) {
            showToast("Please add at least one ingredient")
            return
        }

        if (recipeDescription.isEmpty()) {
            showToast("Please add a description")
            return
        }

        if (steps.isEmpty()) {
            showToast("Please add at least one step")
            return
        }

        // Show loading state
        setLoadingState(true)
        showLoadingDialog()

        // Start coroutine to handle the upload
        lifecycleScope.launch {
            val imageUrl = if (imageUri != null) {
                uploadImage() // Upload image and get URL
            } else {
                null // No image to upload
            }

            saveRecipeToFirestore(imageUrl) // Save recipe to Firestore
            dismissLoadingDialog()
        }
    }

    private suspend fun uploadImage(): String? {
        return withContext(Dispatchers.IO) {
            try {
                // Ensure imageUri is not null
                val uri = imageUri ?: return@withContext null

                // Generate a unique filename
                val filename = "recipe_image_${UUID.randomUUID()}.jpg"

                // Create a reference to Firebase Storage
                val imageRef = storage.reference.child("recipe_images/$filename")

                // Upload image to Firebase Storage
                val uploadTask = imageRef.putFile(uri).await() // Use `.await()` for coroutines
                imageRef.downloadUrl.await().toString() // Get the download URL
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showToast("Image upload failed: ${e.localizedMessage}")
                }
                null
            }
        }
    }


    private fun showLoadingDialog() {
        loadingDialog = DialogLoadingFragment()
        loadingDialog.show(parentFragmentManager, "LoadingDialog")
    }

    private fun dismissLoadingDialog() {
        if (::loadingDialog.isInitialized) {
            loadingDialog.dismiss()
        }
    }

//    private fun uploadImageAndSaveRecipe() {
//        // Ensure imageUri is not null
//        val uri = imageUri ?: run {
//            showToast("No image selected")
//            setLoadingState(false)
//            return
//        }
//
//        // Generate a unique filename
//        val filename = "recipe_image_${UUID.randomUUID()}.jpg"
//
//        // Create a reference to Firebase Storage
//        val imageRef = storage.reference.child("recipe_images/$filename")
//
//        // Upload image to Firebase Storage
//        imageRef.putFile(uri)
//            .addOnSuccessListener { taskSnapshot ->
//                // Get the download URL
//                imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
//                    // Save recipe with image URL
//                    saveRecipeToFirestore(downloadUri.toString())
//                }
//            }
//            .addOnFailureListener { exception ->
//                // More detailed error logging
//                Log.e("ImageUpload", "Upload failed", exception)
//                showToast("Image upload failed: ${exception.localizedMessage}")
//                setLoadingState(false)
//            }
//    }

    private suspend fun saveRecipeToFirestore(imageUrl: String?) {
        withContext(Dispatchers.IO) {
            try {
                val recipe = Recipe(
                    id = UUID.randomUUID().toString(),
                    userName = auth.currentUser?.displayName ?: "",
                    recipeName = binding.etRecipeName.text.toString().trim(),
                    recipeDescription = binding.etRecipeDescription.text.toString().trim(),
                    ingredients = ingredients.toList(),
                    steps = steps.toList(),
                    imageUri = imageUrl, // Can be null if no image
                    prepTime = null,
                    difficulty = binding.spinnerDifficulty.selectedItem.toString(),
                    typeCuisine = binding.spinnerCuisine.selectedItem.toString(),
                    userId = auth.currentUser?.uid ?: "",
                    vegan = binding.cbVegan.isChecked,
                    vegetarian = binding.cbVegetarian.isChecked
                )

                db.collection("recipes").document(recipe.id).set(recipe).await() // Save to Firestore
                withContext(Dispatchers.Main) {
                    showToast("Recipe saved successfully!")
                    setLoadingState(false)
                    findNavController().popBackStack()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showToast("Error saving recipe: ${e.message}")
                    setLoadingState(false)
                }
            }
        }
    }



//    private fun saveRecipeToFirestore(imageUrl: String?) {
//        val userId = auth.currentUser?.uid
//
//        val firestore = FirebaseFirestore.getInstance()
//        // Create recipe object
//
//        val recipe = Recipe(
//            id = UUID.randomUUID().toString(),
//            userName = auth.currentUser?.displayName ?: "",
//            recipeName = binding.etRecipeName.text.toString().trim(),
//            recipeDescription = binding.etRecipeDescription.text.toString().trim(),
//            ingredients = ingredients.toList(),
//            steps = steps.toList(),
//            imageUri = imageUrl, // Can be null if no image
//            prepTime = null,
//            difficulty = binding.spinnerDifficulty.selectedItem.toString(),
//            typeCuisine = binding.spinnerCuisine.selectedItem.toString(),
//            userId = auth.currentUser?.uid ?: "",
//            vegan = binding.cbVegan.isChecked,
//            vegetarian = binding.cbVegetarian.isChecked
//        )
//
//        // Save to Firestore
//        db.collection("recipes")
//            .document(recipe.id)
//            .set(recipe)
//            .addOnSuccessListener {
//                showToast("Recipe saved successfully!")
//                setLoadingState(false)
//                findNavController().popBackStack()
//            }
//            .addOnFailureListener { e ->
//                showToast("Error saving recipe: ${e.message}")
//                setLoadingState(false)
//            }
//    }

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
            loadingSpinner.visibility = if (isLoading) View.VISIBLE else View.GONE

            btnSaveRecipe.isEnabled = !isLoading
            btnAddIngredient.isEnabled = !isLoading
            btnAddStep.isEnabled = !isLoading
            etRecipeName.isEnabled = !isLoading
            etIngredient.isEnabled = !isLoading
            etStep.isEnabled = !isLoading
            btnUploadImage.isEnabled = !isLoading
            spinnerCuisine.isEnabled = !isLoading
            spinnerDifficulty.isEnabled = !isLoading
            cbVegan.isEnabled = !isLoading
            cbVegetarian.isEnabled = !isLoading
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