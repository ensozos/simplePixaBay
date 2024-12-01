package com.zosimadis.simpleproject.details

import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.Visibility
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.runner.AndroidJUnit4
import com.zosimadis.simpleproject.R
import com.zosimadis.simpleproject.util.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.text.NumberFormat

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class DetailsFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private lateinit var navController: TestNavHostController
    private var fragmentLaunched = false

    @Before
    fun setup() {
        hiltRule.inject()

        navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        val imageId = 123L
        val fragmentArgs = bundleOf("imageId" to imageId)

        launchFragmentInHiltContainer<DetailsFragment>(fragmentArgs) {
            viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                if (viewLifecycleOwner != null) {
                    navController.setGraph(R.navigation.nav_graph)
                    navController.setCurrentDestination(
                        R.id.detailsFragment,
                        fragmentArgs,
                    )
                    Navigation.setViewNavController(requireView(), navController)
                    fragmentLaunched = true
                }
            }
        }

        while (!fragmentLaunched) {
            Thread.sleep(100)
        }
    }

    @Test
    fun whenDataLoaded_showsAllImageDetails() {
        Thread.sleep(1000)

        onView(withId(R.id.imageView))
            .check(matches(isDisplayed()))
        onView(withId(R.id.imageSize))
            .check(matches(isDisplayed()))
        onView(withId(R.id.imageType))
            .check(matches(isDisplayed()))
        onView(withId(R.id.tagsChipGroup))
            .check(matches(isDisplayed()))

        onView(withId(R.id.userName))
            .check(matches(isDisplayed()))
        onView(withId(R.id.views))
            .check(matches(isDisplayed()))
        onView(withId(R.id.likes))
            .check(matches(isDisplayed()))
        onView(withId(R.id.comments))
            .check(matches(isDisplayed()))
        onView(withId(R.id.downloads))
            .check(matches(isDisplayed()))
    }

    @Test
    fun whenDataLoaded_hidesProgressBarAndShowsContent() {
        Thread.sleep(1000)

        onView(withId(R.id.progressBar))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.contentGroup))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun whenDataLoaded_showsFormattedNumbers() {
        Thread.sleep(1000)

        onView(withId(R.id.views))
            .check(matches(withText(isFormattedNumber())))
        onView(withId(R.id.likes))
            .check(matches(withText(isFormattedNumber())))
        onView(withId(R.id.comments))
            .check(matches(withText(isFormattedNumber())))
        onView(withId(R.id.downloads))
            .check(matches(withText(isFormattedNumber())))
    }

    private fun isFormattedNumber(): Matcher<String> {
        return object : org.hamcrest.TypeSafeMatcher<String>() {
            override fun describeTo(description: org.hamcrest.Description) {
                description.appendText("is a formatted number")
            }

            override fun matchesSafely(item: String): Boolean {
                return try {
                    NumberFormat.getNumberInstance().parse(item) != null
                } catch (e: Exception) {
                    false
                }
            }
        }
    }
}
