package com.example.recipefinder.ui.savedrecipes

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SavedRecipesViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SavedRecipesViewModel::class.java)) {
            return SavedRecipesViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
