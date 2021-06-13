package com.sagarmishra.futsal

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import com.sagarmishra.futsal.api.RetrofitService
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@LargeTest
@RunWith(JUnit4::class)
class PasswordResetTesting {
    @get:Rule
    val testRule = ActivityScenarioRule(LoginActivity::class.java)

    @Test
    fun passwordResetUI()
    {
        onView(withId(R.id.btnForgotPassword))
            .perform(click())

        Thread.sleep(400)

        onView(withId(R.id.etEmail))
            .perform(typeText("sagarcrcoc@gmail.com"))
        closeSoftKeyboard()
        Thread.sleep(400)

        onView(withId(R.id.btnSendMail))
            .perform(click())

        Thread.sleep(5000)
        RetrofitService.resetCode = "1234"

        onView(withId(R.id.etFirst))
            .perform(typeText("1"))
        closeSoftKeyboard()
        Thread.sleep(400)

        onView(withId(R.id.etSecond))
            .perform(typeText("2"))
        closeSoftKeyboard()
        Thread.sleep(400)

        onView(withId(R.id.etThird))
            .perform(typeText("3"))
        closeSoftKeyboard()
        Thread.sleep(400)

        onView(withId(R.id.etFourth))
            .perform(typeText("4"))
        closeSoftKeyboard()
        Thread.sleep(400)

        onView(withId(R.id.btnConfirm))
            .perform(click())

        Thread.sleep(2000)

        onView(withId(R.id.etPassword))
            .perform(typeText("123456"))
        closeSoftKeyboard()
        Thread.sleep(400)

        onView(withId(R.id.etConPassword))
            .perform(typeText("123456"))
        closeSoftKeyboard()
        Thread.sleep(400)

        onView(withId(R.id.btnReset))
            .perform(click())

        Thread.sleep(2000)

        onView(withText("Password Changed"))
            .check(matches(isDisplayed()))
    }
}