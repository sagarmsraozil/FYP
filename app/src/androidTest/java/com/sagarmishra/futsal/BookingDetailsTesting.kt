package com.sagarmishra.futsal

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
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
class BookingDetailsTesting {
    @get:Rule
    val testRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun bookingDetailsUI()
    {
        runBlocking {
            RetrofitService.online = true
            val userRepository = UserRepository()
            val response = userRepository.authenticateUser("sagarmsra","123456")
            RetrofitService.token = "Bearer "+response.token
            StaticData.user = response.data
        }

        Thread.sleep(2000)

        onView(withId(R.id.nav_bookings))
            .perform(ViewActions.click())
        Thread.sleep(400)

        onView(withId(R.id.recycler))
            .perform(RecyclerViewActions.actionOnItemAtPosition<MyBookingsAdapter.MyBookingsViewHolder>(0,CustomAction.clickItemWithId(R.id.tvDetails)))
        Thread.sleep(400)

        Thread.sleep(2000)

        onView(withText("Booking Details"))
            .check(matches(isDisplayed()))
    }
}