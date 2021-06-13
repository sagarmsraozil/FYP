package com.sagarmishra.futsal

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import com.sagarmishra.futsal.Repository.UserRepository
import com.sagarmishra.futsal.api.RetrofitService
import com.sagarmishra.futsal.model.StaticData
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@LargeTest
@RunWith(JUnit4::class)
class PasswordChangeTesting {
    @get:Rule
    val testRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun passwordChangeUI()
    {
        runBlocking {
            RetrofitService.online = true
            val repo = UserRepository()
            val response = repo.authenticateUser("sagarmsra","123456")
            RetrofitService.token = "Bearer "+response.token
            StaticData.user= response.data
        }

        Thread.sleep(2000)

        onView(withId(R.id.nav_account))
            .perform(click())
        Thread.sleep(400)

        onView(withId(R.id.btnEdit))
            .perform(click())
        Thread.sleep(400)

        onView(withText("PASSWORD"))
            .perform(click())
        Thread.sleep(400)

        onView(withId(R.id.etCurrent))
            .perform(typeText("123456"))
        closeSoftKeyboard()
        Thread.sleep(400)

        onView(withId(R.id.etPw))
            .perform(typeText("1234567"))
        closeSoftKeyboard()
        Thread.sleep(400)

        onView(withId(R.id.etCpw))
            .perform(typeText("1234567"))
        closeSoftKeyboard()
        Thread.sleep(400)

        onView(withId(R.id.btnChange))
            .perform(click())

        Thread.sleep(2000)

        onView(withText("Password Changed!!"))
            .check(matches(isDisplayed()))

    }
}