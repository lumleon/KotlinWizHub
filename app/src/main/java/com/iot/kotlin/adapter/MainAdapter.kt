package com.iot.kotlin.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.iot.kotlin.R
import com.iot.kotlin.viewholder.MainViewHolder
import kotlinx.android.synthetic.main.include_roomitem.view.*


/**
 *  The MainAdapter acts as the bridge of room list to the view.
 *  @param roomList, a list of room, predefined for main activity
 *
 *  Created by leonlum on 08/08/18.
 */

class MainAdapter(val roomList: MutableList<String>) : RecyclerView.Adapter<MainViewHolder>() {

    // the method to create the list view in the recyclerview
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.list_room, parent, false)
        return MainViewHolder(cellForRow)
    }

    //The business logic to present the person's data from the model to the view
    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val roomlist = roomList.get(position)

        holder.view.textView_RoomName.text = roomlist

        //setup   object to viewholder for accessing details
        holder.roomList = roomList
    }

    // returning the number of Items in the recyclerview
    override fun getItemCount(): Int {
        return roomList.count()
    }

}














