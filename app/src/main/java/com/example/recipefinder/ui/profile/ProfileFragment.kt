package com.example.recipefinder.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.recipefinder.LandingActivity
import com.example.recipefinder.R
import com.example.recipefinder.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        // Initialize ViewModel
        viewModel = ViewModelProvider(
            this,
            ProfileViewModelFactory(requireContext())
        )[ProfileViewModel::class.java]

        // Observe ViewModel
        observeViewModel()

        // Set logout button click listener
        binding.btnLogout.setOnClickListener {
            viewModel.logout()
        }

        binding.btnViewAndDeleteRecipes.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_manageRecipesFragment)
        }
    }

    private fun observeViewModel() {
        viewModel.profileName.observe(viewLifecycleOwner) { name ->
            binding.profileName.text = name
        }

        viewModel.profileEmail.observe(viewLifecycleOwner) { email ->
            binding.profileEmail.text = email
        }

        viewModel.profilePictureUrl.observe(viewLifecycleOwner) { url ->
            if (!url.isNullOrEmpty()) {
                // Use URL to fetch the image and set it directly to the ImageView
                val uri = Uri.parse(url)
                binding.profilePicture.setImageURI(uri)
            } else {
                // Set default profile picture if URL is empty or null
                binding.profilePicture.setImageResource(R.drawable.baseline_person_24)
            }
        }


        viewModel.logoutEvent.observe(viewLifecycleOwner) { shouldLogout ->
            if (shouldLogout) {
                redirectToLogin()
                viewModel.clearLogoutEvent()
            }
        }
    }


    private fun redirectToLogin() {
        val intent = Intent(requireContext(), LandingActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
