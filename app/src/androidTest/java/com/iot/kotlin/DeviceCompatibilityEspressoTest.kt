package com.iot.kotlin

import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.util.Log
import com.iot.kotlin.view.MainActivity
import com.iot.kotlin.viewholder.MainViewHolder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class DeviceCompatibilityEspressoTest {

    @Rule
    @JvmField
    val rule = ActivityTestRule(MainActivity::class.java)


    //number of milli second to wait for loading data from REST API
    val waittime: Long = 3000

    @Test
    fun DeviceCompatibility_success() {
        Log.e("@Test", "Performing app loading success test")



        Espresso.onView(ViewMatchers.withId(R.id.recyclerView_main))
                .perform(RecyclerViewActions.actionOnItemAtPosition<MainViewHolder>(0, ViewActions.click()))

        Thread.sleep(waittime)

    }
}