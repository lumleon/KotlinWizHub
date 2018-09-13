package com.iot.kotlin.viewholder

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.View
import com.iot.kotlin.view.FixtureActivity

/**
    *  The viewholder for each row of room, extend from the recyclerview holder.
    *  @param view
    *  @param roomList, the list of room defned in the mainactivity
    *
    */
class MainViewHolder(val view: View, var roomList: MutableList<String>? = null) : RecyclerView.ViewHolder(view) {

    companion object {
        val ROOM_KEY = "ROOM_TRANSACITON"
    }

    init {

        view.setOnClickListener {

            // setup intent and pass to fixture activity
            val intent = Intent(view.context, FixtureActivity::class.java)
            intent.putExtra(ROOM_KEY, roomList!!.get(position).toString())
            view.context.startActivity(intent)

        }
    }
}
