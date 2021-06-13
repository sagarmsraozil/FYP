package com.sagarmishra.futsal

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import com.sagarmishra.futsal.Repository.UserRepository
import com.sagarmishra.futsal.adapter.MyBookingsAdapter
import com.sagarmishra.futsal.api.RetrofitService
import com.sagarmishra.futsal.model.StaticData
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@LargeTest
@RunWith(JUnit4::class)
class DeleteBookingTesting {
    @get:Rule
    val testRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testDeleteBookingUI()
    {
        runBlocking {
            RetrofitService.online = true
            var userRepo = UserRepository()
            var response = userRepo.authenticateUser("sagarmsra","123456")
            RetrofitService.token = "Bearer "+response.token
            StaticData.user = response.data
        }
        Thread.sleep(2000)

        onView(withId(R.id.nav_bookings))
            .perform(click())
        Thread.sleep(400)

        onView(withId(R.id.recycler))
            .perform(RecyclerViewActions.actionOnItemAtPosition<MyBookingsAdapter.MyBookingsViewHolder>(0,CustomAction.clickItemWithId(R.id.btnDelete)))

        Thread.sleep(400)

        onView(withText("Yes"))
            .perform(click())

        Thread.sleep(2000)

        onView(withText("Booking deleted successfully!!"))
            .check(matches(isDisplayed()))

    }
}