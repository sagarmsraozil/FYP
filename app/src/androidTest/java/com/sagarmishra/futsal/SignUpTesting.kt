package com.sagarmishra.futsal

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@LargeTest
@RunWith(JUnit4::class)
class SignUpTesting {
    @get:Rule
    val testRule = ActivityScenarioRule(SignUpActivity::class.java)

    @Test
    fun checkSignUpAPI()
    {
        onView(withId(R.id.etFn))
            .perform(typeText("Sagar"))
        closeSoftKeyboard()
        Thread.sleep(400)
        onView(withId(R.id.etLn))
            .perform(typeText("Mishra"))
        closeSoftKeyboard()
        Thread.sleep(400)
        onView(withId(R.id.etEmail))
            .perform(typeText("sagarmishrasagar@gmail.com"))
        closeSoftKeyboard()
        Thread.sleep(400)
        onView(withId(R.id.etUn))
            .perform(scrollTo(), typeText("sagarmsrat"))
        closeSoftKeyboard()
        Thread.sleep(400)
        onView(withId(R.id.etAd))
            .perform(scrollTo(),typeText("Swyambhu"))
        closeSoftKeyboard()
        Thread.sleep(400)
        onView(withId(R.id.etMb))
            .perform(scrollTo(),typeText("9803609163"))
        closeSoftKeyboard()
        Thread.sleep(400)
        onView(withId(R.id.etDOB))
            .perform(scrollTo(), replaceText("1999-12-13"))
        closeSoftKeyboard()
        Thread.sleep(400)
        onView(withId(R.id.etPw))
            .perform(scrollTo(),typeText("123456"))
        closeSoftKeyboard()
        Thread.sleep(400)
        onView(withId(R.id.etCpw))
            .perform(scrollTo(),typeText("123456"))
        closeSoftKeyboard()
        Thread.sleep(400)
        onView((withId(R.id.rbMale)))
            .perform(scrollTo(),click())
        closeSoftKeyboard()
        onView(withId(R.id.cbCheck))
            .perform(scrollTo(),click())
        closeSoftKeyboard()
        onView(withId(R.id.btnRegister))
            .perform(click())

        closeSoftKeyboard()
        Thread.sleep(2000)

        onView(withText("Skip"))
            .perform(click())

        onView(withText("Registration Success!!"))
            .check(matches(isDisplayed()))

    }
}