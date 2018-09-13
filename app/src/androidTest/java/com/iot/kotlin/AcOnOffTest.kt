package com.iot.kotlin

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.util.Log
import com.iot.kotlin.util.Constant.Companion.TEMPERATURE_THRESHOLD
import com.iot.kotlin.util.PrefHelper
import com.iot.kotlin.view.MainActivity
import com.iot.kotlin.viewholder.MainViewHolder
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)

class AcOnOffTest {

    @Rule
    @JvmField
    val rule = ActivityTestRule(MainActivity::class.java)


    //number of records returned from REST API
    val itemcount: Int = 3

    //number of milli second to wait for loading data from REST API
    val waittime: Long = 3000

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("com.iot.kotlin", appContext.packageName)
    }

    @Test
    fun bedroom_displaysuccess() {
        Log.e("@Test", "Performing bedroom  display checking")

        Thread.sleep(waittime)

        Espresso.onView(ViewMatchers.withText("bedroom"))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }


    @Test
    fun livingroom_displaysuccess() {
        Log.e("@Test", "Performing living-room  display checking")

        Thread.sleep(waittime)

        Espresso.onView(ViewMatchers.withText("living-room"))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }


    @Test
    fun kitchen_displaysuccess() {
        Log.e("@Test", "Performing kitchen room display checking")

        Thread.sleep(waittime)

        Espresso.onView(ViewMatchers.withText("kitchen"))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }


    @Test
    fun click_fixture_display_success0() {
        Log.e("@Test", "Performing scroll and click on bedroom")

        Thread.sleep(waittime)

        //scroll item  and perform click to display details in Recyclerview
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView_main))
                .perform(RecyclerViewActions.actionOnItemAtPosition<MainViewHolder>(0, ViewActions.click()))
        //check "AC" exist
        Espresso.onView(ViewMatchers.withText("AC"))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }


    @Test
    fun click_fixture_display_success1() {
        Log.e("@Test", "Performing scroll and click on living-room")

        Thread.sleep(waittime)

        //scroll item  and perform click to display details in Recyclerview
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView_main))
                .perform(RecyclerViewActions.actionOnItemAtPosition<MainViewHolder>(1, ViewActions.click()))
        Espresso.onView(ViewMatchers.withText("Light"))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun click_fixture_display_success2() {
        Log.e("@Test", "Performing scroll and click on kitchen")

        Thread.sleep(waittime)

        //scroll item  and perform click to display details in Recyclerview
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView_main))
                .perform(RecyclerViewActions.actionOnItemAtPosition<MainViewHolder>(2, ViewActions.click()))
        Espresso.onView(ViewMatchers.withText("Music"))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test

    fun test_AcStatus() {

        val mContext = InstrumentationRegistry.getTargetContext()

        val keynames = PrefHelper.getSharedPreferences(mContext).all
        Log.d("AllKeyTAG", keynames.toString())

        if (PrefHelper.getTemperature(mContext)!!.toDouble() > TEMPERATURE_THRESHOLD)
            assertEquals("on", PrefHelper.getKey(mContext, "bedroom_ac"))
        else assertEquals("off", PrefHelper.getKey(mContext, "bedroom_ac"))

    }


}
