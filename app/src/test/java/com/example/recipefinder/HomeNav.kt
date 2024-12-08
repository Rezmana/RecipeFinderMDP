//import androidx.navigation.NavController
//import androidx.navigation.Navigation
//import androidx.navigation.testing.TestNavHostController
//import androidx.test.core.app.launchFragmentInContainer
//import androidx.test.espresso.Espresso.onView
//import androidx.test.espresso.action.ViewActions.click
//import androidx.test.espresso.matcher.ViewMatchers.withId
//import com.example.recipefinder.R
//import com.example.recipefinder.ui.home.HomeFragment
//import org.junit.Assert.assertEquals
//import org.junit.Test
//
//class HomeFragmentNavigationTest {
//
//    @Test
//    fun testNavigateToBrowseFragment() {
//        // Initialize a TestNavHostController
//        val navController = TestNavHostController(
//            androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().targetContext
//        )
//
//        // Launch the HomeFragment in isolation
//        val scenario = launchFragmentInContainer<HomeFragment>(
//            themeResId = R.style.Theme_RecipeFinder
//        )
//
//        scenario.onFragment { fragment ->
//            // Set up the NavController for the fragment
//            navController.setGraph(R.navigation.nav_graph)
//            Navigation.setViewNavController(fragment.requireView(), navController)
//        }
//
//        // Perform click on the "Browse" button
//        onView(withId(R.id.tv_browse)).perform(click())
//
//        // Assert that the NavController navigates to the expected destination
//        assertEquals(R.id.browseFragment, navController.currentDestination?.id)
//    }
//}
