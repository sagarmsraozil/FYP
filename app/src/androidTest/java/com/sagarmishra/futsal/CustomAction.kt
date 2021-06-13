package com.sagarmishra.futsal

import android.view.View
import android.widget.EditText
import androidx.appcompat.widget.AppCompatSpinner
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher
@Suppress("Deprecation")
class CustomAction {

    companion object{
        fun clickItemWithId(id: Int): ViewAction {
            return object : ViewAction {
                override fun getConstraints(): Matcher<View>? {
                    return null
                }

                override fun getDescription(): String {
                    return "Click on a child view with specified id."
                }

                override fun perform(uiController: UiController, view: View) {
                    val v = view.findViewById<View>(id) as View
                    v.performClick()
                }
            }
        }

        fun isRefreshing(): Matcher<View> {
            return object : BoundedMatcher<View, SwipeRefreshLayout>(
                SwipeRefreshLayout::class.java) {

                override fun describeTo(description: Description) {
                    description.appendText("is refreshing")
                }

                override fun matchesSafely(view: SwipeRefreshLayout): Boolean {
                    return view.isRefreshing
                }
            }
        }
    }

}