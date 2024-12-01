package com.zosimadis.simpleproject.register

import android.view.View
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.runner.AndroidJUnit4
import com.google.android.material.textfield.TextInputLayout
import com.zosimadis.simpleproject.R
import com.zosimadis.simpleproject.util.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class RegisterFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private lateinit var navController: TestNavHostController

    @Before
    fun setup() {
        hiltRule.inject()

        navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        launchFragmentInHiltContainer<RegisterFragment> {
            viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                if (viewLifecycleOwner != null) {
                    navController.setGraph(R.navigation.nav_graph)
                    navController.setCurrentDestination(R.id.registerFragment)
                    Navigation.setViewNavController(requireView(), navController)
                }
            }
        }
    }

    @Test
    fun whenFieldsEmpty_showsErrors() {
        onView(withId(R.id.registerButton)).perform(click())

        onView(withId(R.id.emailInputLayout))
            .check(matches(hasTextInputLayoutError()))
        onView(withId(R.id.passwordInputLayout))
            .check(matches(hasTextInputLayoutError()))
        onView(withId(R.id.ageInputLayout))
            .check(matches(hasTextInputLayoutError()))
    }

    @Test
    fun whenEmailInvalid_showsEmailError() {
        onView(withId(R.id.emailEditText))
            .perform(typeText("invalid-email"), closeSoftKeyboard())
        onView(withId(R.id.registerButton)).perform(click())

        onView(withId(R.id.emailInputLayout))
            .check(matches(hasTextInputLayoutError()))
    }

    @Test
    fun whenPasswordTooShort_showsPasswordError() {
        onView(withId(R.id.passwordEditText))
            .perform(typeText("12345"), closeSoftKeyboard())
        onView(withId(R.id.registerButton)).perform(click())

        onView(withId(R.id.passwordInputLayout))
            .check(matches(hasTextInputLayoutError()))
    }

    @Test
    fun whenAgeTooYoung_showsAgeError() {
        onView(withId(R.id.ageEditText))
            .perform(typeText("17"), closeSoftKeyboard())
        onView(withId(R.id.registerButton)).perform(click())

        onView(withId(R.id.ageInputLayout))
            .check(matches(hasTextInputLayoutError()))
    }

    @Test
    fun whenValidInput_navigatesToHome() {
        onView(withId(R.id.emailEditText))
            .perform(typeText("test@example.com"), closeSoftKeyboard())
        onView(withId(R.id.passwordEditText))
            .perform(typeText("password123"), closeSoftKeyboard())
        onView(withId(R.id.ageEditText))
            .perform(typeText("25"), closeSoftKeyboard())

        onView(withId(R.id.registerButton)).perform(click())

        Thread.sleep(1000)

        assertEquals(R.id.homeFragment, navController.currentDestination?.id)
    }

    @Test
    fun whenLoginPromptClicked_navigatesToLogin() {
        onView(withId(R.id.loginTextView)).perform(click())

        Thread.sleep(1000)

        assertEquals(navController.currentDestination?.id, R.id.loginFragment)
    }

    @Test
    fun whenLoading_showsProgressAndDisablesInputs() {
        onView(withId(R.id.emailEditText))
            .perform(typeText("test@example.com"), closeSoftKeyboard())
        onView(withId(R.id.passwordEditText))
            .perform(typeText("password123"), closeSoftKeyboard())
        onView(withId(R.id.ageEditText))
            .perform(typeText("25"), closeSoftKeyboard())

        onView(withId(R.id.registerButton)).perform(click())

        onView(withId(R.id.progressBar))
            .check(matches(isDisplayed()))
        onView(withId(R.id.emailEditText))
            .check(matches(not(isEnabled())))
        onView(withId(R.id.passwordEditText))
            .check(matches(not(isEnabled())))
        onView(withId(R.id.ageEditText))
            .check(matches(not(isEnabled())))
        onView(withId(R.id.registerButton))
            .check(matches(not(isEnabled())))
        onView(withId(R.id.loginTextView))
            .check(matches(not(isEnabled())))
    }

    private fun hasTextInputLayoutError(): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("has error text")
            }

            override fun matchesSafely(item: View): Boolean {
                if (item !is TextInputLayout) return false
                val error = item.error ?: return false
                return error.isNotEmpty()
            }
        }
    }
}
