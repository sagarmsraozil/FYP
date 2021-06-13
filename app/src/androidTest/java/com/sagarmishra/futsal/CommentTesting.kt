package com.sagarmishra.futsal

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import com.sagarmishra.futsal.Repository.UserRepository
import com.sagarmishra.futsal.adapter.CommentFutsalAdapter
import com.sagarmishra.futsal.api.RetrofitService
import com.sagarmishra.futsal.model.StaticData
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@LargeTest
@RunWith(JUnit4::class)
class CommentTesting {
    @get:Rule
    val testRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun commentTestingUI()
    {
        runBlocking {
            RetrofitService.online = true
            var userRepo = UserRepository()
            var response = userRepo.authenticateUser("sagarmsra","123456")
            RetrofitService.token = "Bearer "+response.token
            StaticData.user = response.data
        }
        Thread.sleep(2000)
        onView(withId(R.id.nav_chats))
            .perform(click())
        Thread.sleep(400)

        onView(withId(R.id.recycler))
            .perform(RecyclerViewActions.actionOnItemAtPosition<CommentFutsalAdapter.CommentFutsalViewHolder>(0,CustomAction.clickItemWithId(R.id.ivPopUp)))

        Thread.sleep(400)

        onView(withText("View Comments"))
            .perform(click())

        Thread.sleep(400)

        onView(withId(R.id.etComment))
            .perform(typeText("Awesome Futsal of Nepal"))
        closeSoftKeyboard()
        Thread.sleep(400)

        onView(withId(R.id.tvSend))
            .perform(click())

        Thread.sleep(2000)


        onView(withText("Awesome Futsal of Nepal"))
            .check(matches(isDisplayed()))

    }
}