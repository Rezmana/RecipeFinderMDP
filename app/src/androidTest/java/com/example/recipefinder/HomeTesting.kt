//import androidx.test.ext.junit.runners.AndroidJUnit4
//import androidx.test.platform.app.InstrumentationRegistry
//import androidx.test.rule.ActivityTestRule
//import org.junit.Assert.assertEquals
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//
//@RunWith(AndroidJUnit4::class)
//class ExampleInstrumentedTest {
//
//    @Test
//    fun useAppContext() {
//        // Context of the app under test.
//        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
//        assertEquals("com.example.recipefinder", appContext.packageName)
//    }
//
//    @get:Rule
//    val activityRule = ActivityTestRule(MainActivity::class.java)
//
//    @Test
//    fun testCommunityButtonClick() {
//        // Perform click on the "Community" button
//        onView(withId(R.id.tv_community)).perform(click())
//
//        // Check if the expected toast message is displayed
//        onView(withText("Community clicked")).inRoot(ToastMatcher())
//            .check(matches(isDisplayed()))
//    }
//
//
//}
