package com.example.recipefinder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipefinder.databinding.FragmentAllergiesBinding

class FragmentAllergies : Fragment() {
    private lateinit var binding: FragmentAllergiesBinding
    private val allergies = listOf(
        AllergyModel("Peanuts"),
        AllergyModel("Tree Nuts"),
        AllergyModel("Milk"),
        AllergyModel("Eggs"),
        AllergyModel("Soy"),
        AllergyModel("Wheat"),
        AllergyModel("Fish"),
        AllergyModel("Shellfish")
    )

    private lateinit var allergyAdapter: AllergyAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllergiesBinding.inflate(inflater, container, false)

        setupRecyclerView()
        setupHaveAllergiesCheckbox()

        // Initially hide RecyclerView
        binding.allergyRecyclerView.visibility = View.GONE

        return binding.root
    }

    private fun setupRecyclerView() {
        allergyAdapter = AllergyAdapter(allergies) { selectedAllergies ->
            updateContinueButtonState(selectedAllergies)
        }

        binding.allergyRecyclerView.apply {
            adapter = allergyAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupHaveAllergiesCheckbox() {
        // Rename the checkbox ID in XML from noAllergiesCheckbox to haveAllergiesCheckbox
        binding.AllergiesButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.allergyRecyclerView.visibility = View.VISIBLE
                // Reset any previous selections when showing
                allergyAdapter.clearSelections()
            } else {
                binding.allergyRecyclerView.visibility = View.GONE
                allergyAdapter.clearSelections()
            }
            updateContinueButtonState(emptyList())
        }
    }

    private fun updateContinueButtonState(selectedAllergies: List<AllergyModel>) {
        // Enable continue button if allergies are selected when checkbox is checked
        val hasSelectedAllergies = selectedAllergies.isNotEmpty()
        binding.continueButton.isEnabled = if (binding.AllergiesButton.isChecked) {
            hasSelectedAllergies
        } else {
            true // Enable button when user indicates no allergies
        }
    }
}
