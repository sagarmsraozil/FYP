package com.sagarmishra.futsal

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import com.sagarmishra.futsal.Repository.UserRepository
import com.sagarmishra.futsal.adapter.FutsalAdapter
import com.sagarmishra.futsal.adapter.InstanceAdapter
import com.sagarmishra.futsal.adapter.WeekAdapter
import com.sagarmishra.futsal.api.RetrofitService
import com.sagarmishra.futsal.entityapi.Futsal
import com.sagarmishra.futsal.model.StaticData
import kotlinx.coroutines.runBlocking
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@LargeTest
@RunWith(JUnit4::class)
class BookingTesting {
    @get:Rule
    val testRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun bookingTestingUI()
    {

        runBlocking {
            RetrofitService.online = true
            var userRepo = UserRepository()
            var response = userRepo.authenticateUser("sagarmsra","123456")
            RetrofitService.token = "Bearer "+response.token
            StaticData.user = response.data
        }
       Thread.sleep(2000)
        onView(withId(R.id.nav_home))
            .perform(click())
        Thread.sleep(400)

        onView(withId(R.id.recycler))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<FutsalAdapter.FutsalViewholder>(0, CustomAction.clickItemWithId(R.id.btnBook))
            )

        Thread.sleep(400)

        onView(withId(R.id.recyclerDays))
            .perform(RecyclerViewActions.actionOnItemAtPosition<WeekAdapter.WeekViewHolder>(2,CustomAction.clickItemWithId(R.id.linearLayout)))

        Thread.sleep(400)

        onView(withId(R.id.recyclerTime))
            .perform(RecyclerViewActions.actionOnItemAtPosition<InstanceAdapter.InstanceViewHolder>(4,CustomAction.clickItemWithId(R.id.btnBook)))

        Thread.sleep(400)

        onView(withId(R.id.etMobileNumber))
            .perform(scrollTo(),typeText("9803609163"))

        closeSoftKeyboard()
        Thread.sleep(400)

        onView(withId(R.id.etTeam))
            .perform(scrollTo(),typeText("9803609162"))

        closeSoftKeyboard()
        Thread.sleep(400)


        onView(withId(R.id.cbCheckBox))
            .perform(scrollTo(),click())

        Thread.sleep(400)

        onView(withId(R.id.btnBook))
            .perform(scrollTo(), click())

        onView(withId(R.id.etConfirm))
            .perform(typeText("confirm"))

        closeSoftKeyboard()
        Thread.sleep(400)

        onView(withId(R.id.btnConfirm))
            .perform(click())


        Thread.sleep(2000)

        onView(withText("Booking Success"))
            .check(matches(isDisplayed()))


    }


}