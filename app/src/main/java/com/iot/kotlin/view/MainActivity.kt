package com.iot.kotlin.view

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.*
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.iot.kotlin.R
import com.iot.kotlin.adapter.MainAdapter
import com.iot.kotlin.service.MyJobService
import com.iot.kotlin.util.UiHelper.showNetworkAlertDialog
import kotlinx.android.synthetic.main.content_main.*
import java.util.*


/**
 * The main activity  that  schedule job to check current temperatue and list the  predefined room
 *
 * Created by leonlum on 08/08/18.
 */
class MainActivity : AppCompatActivity() {

    private val TAG = "mainActivity"
    private var mAdapter: MainAdapter? = null
    private var mRoomList: ArrayList<String>? = null
    private val ROOM_VALUE = arrayListOf("bedroom", "living-room", "kitchen")
    private val JOB_ID = 1001 // job id for job scheduler
    private val REFRESH_INTERVAL = (30 * 60 * 1000).toLong()// set temperature retrieval from metaweather in 30 minutes

    //set broadcastReceiver to check internet connection
    private var mBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val notConnected = intent.getBooleanExtra(ConnectivityManager
                    .EXTRA_NO_CONNECTIVITY, false)
            if (notConnected) {
                disconnected()
            } else {
                connected()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        //setup toolbar fancy stuff
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle(R.string.toolbar_title_main)

        //initialization
        mRoomList = ROOM_VALUE
        mAdapter = MainAdapter(mRoomList!!)

        //set recyclerview properties
        recyclerView_main.layoutManager = LinearLayoutManager(this)
        recyclerView_main!!.itemAnimator = DefaultItemAnimator()
        recyclerView_main!!.adapter = mAdapter

        //schedule app to get latest temperature
        scheduleJob()


    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {
            R.id.schedule_job -> {

                //     job scheduler for get temperature periodically
                scheduleJob()
                return true
            }
            R.id.cancel_job -> {

                //cancel job scheduler
                cancelJob()
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun disconnected() {

        showNetworkAlertDialog(this)
    }

    private fun connected() {

        //get current temperature
        //   GetTemperatureController.getCurrentTemp(applicationContext)

    }

    override fun onStart() {
        super.onStart()
        registerReceiver(mBroadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(mBroadcastReceiver)


    }

    //Method to schedule job in background process
    fun scheduleJob() {

        //first cancel all job
        cancelJob()

        //then reschedule the job
        val componentName = ComponentName(this, MyJobService::class.java)
        val info = JobInfo.Builder(JOB_ID, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPersisted(true)
                .setPeriodic(REFRESH_INTERVAL)
                .build()

        val scheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val resultCode = scheduler.schedule(info)
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d(TAG, "Job scheduled")
        } else {
            Log.d(TAG, "Job scheduling failed")
        }
    }

    //method to cancel scheduled job
    fun cancelJob() {
        val scheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        scheduler.cancel(JOB_ID)
        Log.d(TAG, "Job cancelled")
    }

}


