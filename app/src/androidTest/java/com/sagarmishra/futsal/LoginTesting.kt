package com.sagarmishra.futsal

import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@LargeTest
@RunWith(JUnit4::class)
class LoginTesting {
    @get:Rule
    val testRule = ActivityScenarioRule(LoginActivity::class.java)

    @Test
    fun checkLoginUI()
    {
        onView(withId(R.id.etUsername))
            .perform(typeText("sagarmsra"))
        closeSoftKeyboard()
        Thread.sleep(500)
        onView(withId(R.id.etPassword))
            .perform(typeText("123456"))
        closeSoftKeyboard()
        Thread.sleep(500)
        onView(withId(R.id.btnLogin))
            .perform(click())
        Thread.sleep(2000)

        onView(withId(R.id.navView))
            .check(matches(isDisplayed()))
    }
}