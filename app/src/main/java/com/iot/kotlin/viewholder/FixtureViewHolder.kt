package com.iot.kotlin.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import com.iot.kotlin.controller.TurnFixtureOnOffController
import com.iot.kotlin.util.PrefHelper
import kotlinx.android.synthetic.main.include_fixtureitem.view.*


/**  The viewholder for each row of fixture in room, extend from the recyclerview holder.
 *  @param view
 *  @param fixtureList, the object from result node in the JSON
 *
 */
class FixtureViewHolder(val view: View, var fixtureList: MutableList<String>? = null) : RecyclerView.ViewHolder(view) {

    lateinit var mRoomKey: String
    lateinit var mFixtureName: String
    lateinit var mFixtureKey: String


    init {
        //set click listener to call appropriate API for turning on/off the fixture
        view.onOffSwitch.setOnClickListener {
            mRoomKey = view.textView_RoomKey.text.toString().toLowerCase()
            mFixtureName = view.textView_FixtureName.text.toString().toLowerCase()
            mFixtureKey = mRoomKey + "_" + mFixtureName
            if (!view.onOffSwitch.isChecked)
                TurnFixtureOnOffController.turnFixtureOnOffinUI(view, mRoomKey, mFixtureName, "off")
            else
                TurnFixtureOnOffController.turnFixtureOnOffinUI(view, mRoomKey, mFixtureName, "on")
        }

        //Testing for onSHaredPreferenceChangeListener
        view.setOnClickListener {
            PrefHelper.storeTemperature(view.context, "33")
           PrefHelper.storeKey(view.context, "bedroom_ac", "off")
        }
    }
}




