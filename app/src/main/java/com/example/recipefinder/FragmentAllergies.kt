package com.example.recipefinder

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

//class FragmentAllergies : Fragment() {
//    private lateinit var binding: FragmentAllergiesBinding
//
//    private lateinit var recyclerView: RecyclerView
//    private val db = FirebaseFirestore.getInstance()
//    private val auth = FirebaseAuth.getInstance()
//    private lateinit var allergyAdapter: AllergyAdapter
//
//    private val allergies = listOf(
//        AllergyModel("Peanuts"),
//        AllergyModel("Tree Nuts"),
//        AllergyModel("Milk"),
//        AllergyModel("Eggs"),
//        AllergyModel("Soy"),
//        AllergyModel("Wheat"),
//        AllergyModel("Fish"),
//        AllergyModel("Shellfish")
//    )
//
//    private val selectedAllergies = mutableListOf<String>()
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        binding = FragmentAllergiesBinding.inflate(inflater, container, false)
//
//        view?.findViewById<Button>(R.id.continueButton).setOnClickListener {
//            proceedWithRegistration()
//
//        allergyAdapter = AllergyAdapter(allergies, selectedAllergies)
//        recyclerView.adapter = allergyAdapter
//        setupRecyclerView()
//        setupHaveAllergiesCheckbox()
//
//        // Initially hide RecyclerView
//        binding.allergyRecyclerView.visibility = View.GONE
//
//        return binding.root
//    }
//
//    private fun setupRecyclerView() {
//        allergyAdapter = AllergyAdapter(allergies) { selectedAllergies ->
//            updateContinueButtonState(selectedAllergies)
//        }
//
//        binding.allergyRecyclerView.apply {
//            adapter = allergyAdapter
//            layoutManager = LinearLayoutManager(requireContext())
//        }
//    }
//
//    private fun setupHaveAllergiesCheckbox() {
//        // Rename the checkbox ID in XML from noAllergiesCheckbox to haveAllergiesCheckbox
//        binding.AllergiesButton.setOnCheckedChangeListener { _, isChecked ->
//            if (isChecked) {
//                binding.allergyRecyclerView.visibility = View.VISIBLE
//                // Reset any previous selections when showing
//                allergyAdapter.clearSelections()
//            } else {
//                binding.allergyRecyclerView.visibility = View.GONE
//                allergyAdapter.clearSelections()
//            }
//            updateContinueButtonState(emptyList())
//        }
//    }
//
//    private fun saveUserAllergies() {
//        val userId = auth.currentUser?.uid
//        if (userId == null) {
//            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        // Save selected allergies to Firestore
//        val userRef = db.collection("users").document(userId)
//        userRef.update("allergies", selectedAllergies)
//            .addOnSuccessListener {
//                Toast.makeText(requireContext(), "Allergies saved successfully!", Toast.LENGTH_SHORT).show()
//            }
//            .addOnFailureListener { e ->
//                Toast.makeText(requireContext(), "Failed to save allergies: ${e.message}", Toast.LENGTH_SHORT).show()
//            }
//    }
//
//    private fun updateContinueButtonState(selectedAllergies: List<AllergyModel>) {
//        // Enable continue button if allergies are selected when checkbox is checked
//        val hasSelectedAllergies = selectedAllergies.isNotEmpty()
//        binding.continueButton.isEnabled = if (binding.AllergiesButton.isChecked) {
//            hasSelectedAllergies
//        } else {
//            true // Enable button when user indicates no allergies
//        }
//    }
//}

    class FragmentAllergies : Fragment() {

        private lateinit var recyclerView: RecyclerView
        private lateinit var allergyAdapter: AllergyAdapter

        private val availableAllergies = listOf("Nuts", "Dairy", "Gluten", "Shellfish", "Eggs", "Soy")
        private val selectedAllergies = mutableListOf<String>()

        private val db = FirebaseFirestore.getInstance()
        private val auth = FirebaseAuth.getInstance()

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            return inflater.inflate(R.layout.fragment_allergies, container, false)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            recyclerView = view.findViewById(R.id.allergyRecyclerView)
            recyclerView.layoutManager = LinearLayoutManager(requireContext())

            // Initialize the adapter
            allergyAdapter = AllergyAdapter(availableAllergies, selectedAllergies)
            recyclerView.adapter = allergyAdapter

            // Save button click listener
            view.findViewById<Button>(R.id.continueButtonAllergies).setOnClickListener {
                saveUserAllergies()
            }
        }

        private fun saveUserAllergies() {
            val userId = auth.currentUser?.uid
            if (userId == null) {
                Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
                return
            }

            // Save selected allergies to Firestore
            val userRef = db.collection("users").document(userId)
            userRef.update("allergies", selectedAllergies)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Allergies saved successfully!", Toast.LENGTH_SHORT).show()
//                    findNavController().navigate(R.id.)
                    val intent = Intent(activity, AppActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Failed to save allergies: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
