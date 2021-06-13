package com.sagarmishra.futsal

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
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
class ProfilePictureDeleteorChangeTesting {
    @get:Rule
    val testRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun profilePictureChangeUI()
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
            .perform(ViewActions.click())
        Thread.sleep(400)

        onView(withId(R.id.btnEdit))
            .perform(ViewActions.click())
        Thread.sleep(400)

        onView(withText("Profile Picture"))
            .perform(click())
        Thread.sleep(400)

        onView(withId(R.id.btnAdd))
            .perform(click())

        Thread.sleep(400)

        onView(withText("Continue"))
            .perform(click())

        Thread.sleep(2000)

        onView(withText("Ok"))
            .perform(click())

        Thread.sleep(400)

        onView(withId(R.id.btnEdit))
            .check(matches(isDisplayed()))

    }
}