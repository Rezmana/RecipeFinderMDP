package com.example.recipefinder.ui.createrecipe

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.recipefinder.DialogLoadingFragment
import com.example.recipefinder.R
import com.example.recipefinder.databinding.FragmentCreateRecipeBinding

class CreateRecipeFragment : Fragment() {
    private var _binding: FragmentCreateRecipeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CreateRecipeViewModel
    private lateinit var loadingDialog : DialogLoadingFragment

    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateRecipeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[CreateRecipeViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSpinners()
        setupUnitSpinner()
        setupClickListeners()
        observeViewModel()
    }

    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            data?.data?.let { uri ->
                imageUri = uri
                binding.ivRecipeImage.setImageURI(uri) // Display selected image
            } ?: run {
                Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupSpinners() {
        // Cuisine Spinner
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.cuisine_types, // Ensure this array is defined in strings.xml
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerCuisine.adapter = adapter
        }

        // Difficulty Spinner
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.difficulty_levels, // Ensure this array is defined in strings.xml
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerDifficulty.adapter = adapter
        }
    }

    private fun setupUnitSpinner() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.measurements, // Ensure this array is defined in strings.xml
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerUnit.adapter = adapter
        }
    }

    private fun setupClickListeners() {
        binding.btnUploadImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
            }
            imagePickerLauncher.launch(intent)
        }

        binding.btnAddIngredient.setOnClickListener {
            val ingredientName = binding.etIngredient.text.toString().trim()
            val quantity = binding.etIngredientQuantity.text.toString().trim()
            val unit = binding.spinnerUnit.selectedItem.toString()

            if (ingredientName.isNotEmpty() && quantity.isNotEmpty()) {
                val ingredient = "$quantity $unit $ingredientName"
                viewModel.addIngredient(ingredient)
                binding.etIngredient.text.clear()
                binding.etIngredientQuantity.text.clear()
            } else {
                if (ingredientName.isEmpty()) binding.etIngredient.error = "Enter ingredient name"
                if (quantity.isEmpty()) binding.etIngredientQuantity.error = "Enter quantity"
            }
        }

        binding.btnSaveRecipe.setOnClickListener {
            val recipeName = binding.etRecipeName.text.toString().trim()
            val recipeDescription = binding.etRecipeDescription.text.toString().trim()
            val typeCuisine = binding.spinnerCuisine.selectedItem.toString()
            val difficulty = binding.spinnerDifficulty.selectedItem.toString()
            viewModel.saveRecipe(recipeName, recipeDescription, imageUri, typeCuisine, difficulty)
        }

        // Add Preparation Step
        binding.btnAddPrepStep.setOnClickListener {
            val prepStep = binding.etPrepStep.text.toString().trim()
            if (prepStep.isNotEmpty()) {
                viewModel.addPrepStep(prepStep)
                binding.etPrepStep.text.clear()
            } else {
                Toast.makeText(requireContext(), "Please enter a preparation step", Toast.LENGTH_SHORT).show()
            }
        }
        // Add Cooking Step
        binding.btnAddCookingStep.setOnClickListener {
            val cookingStep = binding.etCookingStep.text.toString().trim()
            if (cookingStep.isNotEmpty()) {
                viewModel.addCookingStep(cookingStep)
                binding.etCookingStep.text.clear()
            } else {
                Toast.makeText(requireContext(), "Please enter a cooking step", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading) {
                showLoadingDialog()
            } else {
                dismissLoadingDialog()
            }
        })
        viewModel.ingredients.observe(viewLifecycleOwner) { ingredients ->
            binding.tvIngredientsPreview.text = ingredients.joinToString("\n") { "• $it" }
        }

        viewModel.prepSteps.observe(viewLifecycleOwner) { steps ->
            binding.tvPrepStepsPreview.text = steps.mapIndexed { index, step ->
                "${index + 1}. $step"
            }.joinToString("\n")
        }

        viewModel.cookingSteps.observe(viewLifecycleOwner) { steps ->
            binding.tvCookingStepsPreview.text = steps.mapIndexed { index, step ->
                "${index + 1}. $step"
            }.joinToString("\n")
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            setLoadingState(isLoading)
        }

        viewModel.toastMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                showToast(it)
                viewModel.clearToastMessage()
            }
        }
    }

    private fun dismissLoadingDialog() {
        if (::loadingDialog.isInitialized) {
            loadingDialog.dismiss()
        }
    }

    private fun showLoadingDialog() {
        loadingDialog = DialogLoadingFragment()
        loadingDialog.show(parentFragmentManager, "LoadingDialog")
    }

    private fun setLoadingState(isLoading: Boolean) {
//        binding.loadingSpinner.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.btnSaveRecipe.isEnabled = !isLoading
        binding.btnAddIngredient.isEnabled = !isLoading
        binding.btnAddPrepStep.isEnabled = !isLoading
        binding.btnUploadImage.isEnabled = !isLoading
        binding.btnAddCookingStep.isEnabled = !isLoading
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

