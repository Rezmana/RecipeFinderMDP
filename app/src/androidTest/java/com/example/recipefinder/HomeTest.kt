package com.example.recipefinder.ui.home

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.recipefinder.AppActivity

import com.example.recipefinder.R
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeFragmentTest {

    private lateinit var scenario: ActivityScenario<AppActivity>

    @Before
    fun setup() {
        // Launch the MainActivity
        scenario = ActivityScenario.launch(AppActivity::class.java)
    }

    @Test
    fun testBrowseButtonExists() {
        // Check if the Browse button is displayed
        onView(withId(R.id.tv_browse))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testCommunityButtonExists() {
        // Check if the Community button is displayed
        onView(withId(R.id.tv_community))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testBrowseButtonNavigation() {
        // Perform click on Browse TextView
        onView(withId(R.id.tv_browse))
            .perform(click())

        // You might want to add a check to verify navigation to the Browse fragment
        // This depends on how your navigation is set up
    }

    @Test
    fun testCommunityButtonClick() {
        // Perform click on Community TextView
        onView(withId(R.id.tv_community))
            .perform(click())

        // Add any specific verification if needed
    }
}