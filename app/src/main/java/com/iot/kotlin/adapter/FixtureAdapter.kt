package com.iot.kotlin.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.iot.kotlin.R
import com.iot.kotlin.util.Constant.Companion.NAME_OF_AC
import com.iot.kotlin.util.PrefHelper
import com.iot.kotlin.viewholder.FixtureViewHolder
import kotlinx.android.synthetic.main.include_fixtureitem.view.*
import kotlin.text.Typography.degree


/**
 *  The fixture Adapter acts as the bridge of fixture data to the view.
 *  @param RoomKey, the string for the room in the REST Api
 *  @param fixturelist, an array of fixture returned from the GSON object when parsing the REST API
 *
 *  Created by leonlum on 08/08/18.
 */

class FixtureAdapter(val roomKey: String, val fixtureList: MutableList<String>) : RecyclerView.Adapter<FixtureViewHolder>() {

    lateinit var mFixtureKey: String
    lateinit var mFixturelist: String
    lateinit var mTemperatureValue: String
    // the method to create the list view in the recyclerview
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FixtureViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.list_fixture, parent, false)
        return FixtureViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: FixtureViewHolder, position: Int) {
        mFixturelist = fixtureList.get(position)
        holder.view.textView_RoomKey.text = roomKey
        holder.view.textView_FixtureName.text = mFixturelist

        mFixtureKey = "${holder.view.textView_RoomKey.text.toString().toLowerCase()}_${holder.view.textView_FixtureName.text.toString().toLowerCase()}"

        //set the text of switch widget when checked or not
        setIsCheckedListener(holder)

        //show last fixture status
        showlastFixtureStatus(holder)

        //initialize view display temperture for "AC" fixture
        displayACtemp(holder)

    }

    //method to show last fixture on/off status
    fun showlastFixtureStatus(holder: FixtureViewHolder) {

        val fixtureOnOffStatus = PrefHelper.getKey(holder.view.context, mFixtureKey)
        if (fixtureOnOffStatus == "on") {
            holder.view.onOffSwitch.isChecked = true
        } else if (fixtureOnOffStatus == "off") {
            holder.view.onOffSwitch.isChecked = false
        }
    }

    //    Method to display temperature of "AC" fixture & tuen on/off when temperature change
    fun displayACtemp(holder: FixtureViewHolder) {

        mTemperatureValue = PrefHelper.getTemperature(holder.view.context).toString()!!
        val tempFixture = NAME_OF_AC  //Fixture Name to display temperature
        if (holder.view.textView_FixtureName.text != tempFixture) {
            holder.view.tempView.text = ""
        } else {
            holder.view.tempView.text = mTemperatureValue + degree + "C"
        }
    }

    //Method to set the text on switch
    fun setIsCheckedListener(holder: FixtureViewHolder) {
        holder.view.onOffSwitch?.setOnCheckedChangeListener({ _, isChecked ->
            val msg = if (isChecked) "ON" else "OFF"
            holder.view.onOffSwitch.text = msg
        })
    }

    // returning the number of Items in the recyclerview
    override fun getItemCount(): Int {
        return fixtureList.count()
    }

}
















