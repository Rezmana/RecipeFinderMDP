package com.example.recipefinder

import android.os.Bundle
import android.widget.ImageButton
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.recipefinder.databinding.ActivityDashboardappBinding

class AppActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardappBinding
    private lateinit var customBackButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = ActivityDashboardappBinding.inflate(layoutInflater)
        setContentView(binding.root)  // Use binding.root instead of layout resource

        customBackButton = binding.customBackButton

        val navView: BottomNavigationView = binding.navView

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_dashboardapp) as NavHostFragment  // Use correct fragment ID
        val navController = navHostFragment.navController  // Get the navController properly

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_create_recipe,
                R.id.navigation_browse,
                R.id.navigation_saved_recipe,
                R.id.profileFragment
            )
        )

        // Set up Back Button click listener
        customBackButton.setOnClickListener {
            if (!navController.popBackStack()) {
                // If there are no fragments to pop, exit the activity
                finish()
            }
        }

        // Set up the BottomNavigationView with the NavController
        navView.setupWithNavController(navController)
    }
}