package com.iot.kotlin

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.util.Log
import com.iot.kotlin.view.MainActivity
import com.iot.kotlin.viewholder.MainViewHolder
import org.hamcrest.CoreMatchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MainActivityEspressoTest {

    @Rule
    @JvmField
    val rule = ActivityTestRule(MainActivity::class.java)

    //number of records returned from REST API
    val itemcount: Int = 3

    //number of milli second to wait for loading data from REST API
    val waittime: Long = 3000


    @Test
    fun countitem_success() {
        Log.e("@Test", "Counting item in recyclerview")

        Thread.sleep(waittime)

        //check number of item in Recyclerview
        val rviewItemCountAssertion: RecyclerViewItemCountAssertion = RecyclerViewItemCountAssertion.withItemCount(itemcount)
        onView(withId(R.id.recyclerView_main)).check(rviewItemCountAssertion)

    }

    @Test
    fun recyclerViewClickTest() {
        Thread.sleep(waittime)
        val recyclerView = onView(
                allOf(withId(R.id.recyclerView_main), isDisplayed()))
        recyclerView.perform(actionOnItemAtPosition<MainViewHolder>(0, click()))
    }

//




}