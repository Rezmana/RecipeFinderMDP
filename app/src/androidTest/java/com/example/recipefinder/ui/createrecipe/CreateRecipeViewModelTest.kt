package com.example.recipefinder.ui.createrecipe

import androidx.lifecycle.Observer
import androidx.room.InvalidationTracker
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.recipefinder.getOrAwaitValue
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@RunWith(AndroidJUnit4::class)
class CreateRecipeViewModelAndroidTest {

    private lateinit var viewModel: CreateRecipeViewModel
//
//    private lateinit var mockFirestore: FirebaseFirestore
//    private lateinit var mockCollection: CollectionReference
//    private lateinit var mockQuerySnapshot: QuerySnapshot

    @Before
    fun setup() {
//        mockFirestore = mock(FirebaseFirestore::class.java)
//        mockCollection = mock(CollectionReference::class.java)
//        mockQuerySnapshot = mock(QuerySnapshot::class.java)
        viewModel = CreateRecipeViewModel() // Use real Firebase instances
//        `when`(mockFirestore.collection("recipes")).thenReturn(mockCollection)
    }

    @Test
    fun `addIngredient adds ingredient successfully`() {
        // Arrange
        val ingredient = "Tomato"

        // Act
        viewModel.addIngredient(ingredient)
//        val ingredients = viewModel.ingredients.value
        val ingredients = viewModel.ingredients.getOrAwaitValue() // Wait for LiveData to update
        // Assert
        assertEquals(listOf("Tomato"), ingredients)
    }



    @Test
    fun `addPrepStep adds preparation step successfully`() {
        // Arrange
        val step = "Mix ingredients"

        // Act
        viewModel.addPrepStep(step)
//        val steps = viewModel.prepSteps.value
        val steps = viewModel.prepSteps.getOrAwaitValue() // Wait for LiveData to update
        // Assert

        assertEquals(listOf("Mix ingredients"), steps)
    }

    @Test
    fun saveRecipe_savesToFirestoreSuccessfully() {
        // Arrange
        viewModel.addIngredient("Flour")
        viewModel.addPrepStep("Mix ingredients")
        viewModel.addCookingStep("Bake for 30 minutes")

        // Act
        viewModel.saveRecipe(
            recipeName = "Pizza",
            recipeDescription = "Delicious homemade pizza",
            imageUri = null,
            typeCuisine = "Italian",
            difficulty = "Medium"
        )

        // Assert
        // Use LiveData observer to verify success or inspect Firestore
    }
    @OptIn(ExperimentalCoroutinesApi::class)
//    @Test
//    fun `saveRecipe succeeds when all fields are valid`() = runTest {
//        // Arrange
//        val recipeName = "Pasta"
//        val recipeDescription = "Delicious homemade pasta"
//        val cuisine = "Italian"
//        val difficulty = "Medium"
//
//        doNothing().`when`(mockFirestore).collection(anyString())
//
//        // Act
//        viewModel.addIngredient("Flour")
//        viewModel.addPrepStep("Mix ingredients")
//        viewModel.addCookingStep("Cook pasta")
//        viewModel.saveRecipe(recipeName, recipeDescription, null, cuisine, difficulty)
//
//        advanceUntilIdle()
//
//        // Assert
//        val toastMessage = viewModel.toastMessage.getOrAwaitValue()
//        assertEquals("Recipe saved successfully!", toastMessage)
//    }

    @Test
    fun `addCookingStep updates the cooking steps list`() {
        // Arrange
        val step = "Boil the tomatoes"

        // Act
        viewModel.addCookingStep(step)
        val result = viewModel.cookingSteps.getOrAwaitValue()

        // Assert
        assertEquals(listOf("Boil the tomatoes"), result)
    }

    fun `saveRecipe triggers validation error when recipe name is empty`() {
        // Arrange
        val observer = mock(InvalidationTracker.Observer::class.java) as Observer<String?>
        viewModel.toastMessage.observeForever(observer)

        // Act
        viewModel.saveRecipe("", "Description", null, "Italian", "Easy")

        // Assert
        verify(observer).onChanged("Please fill in all required fields")
        viewModel.toastMessage.removeObserver(observer)
    }


}
