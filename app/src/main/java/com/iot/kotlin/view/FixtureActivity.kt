package com.iot.kotlin.view

import android.app.Dialog
import android.app.ProgressDialog
import android.content.*
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import com.iot.kotlin.R
import com.iot.kotlin.adapter.FixtureAdapter
import com.iot.kotlin.api.FixtureListApiService
import com.iot.kotlin.model.SmartRooms
import com.iot.kotlin.util.PrefHelper
import com.iot.kotlin.util.UiHelper
import com.iot.kotlin.viewholder.MainViewHolder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.content_main.*

/**
 *  The activity that shows the detailed information of fixture when selected from the main activity
 *
 *  Created by leonlum on 08/08/18.
 *
 */

class FixtureActivity : AppCompatActivity() {

    private var mAdapter: FixtureAdapter? = null
    private var mFixtureList: ArrayList<String>? = null
    private lateinit var mRoomKey: String
    private var mTemperatureValue: String? = null

    private  lateinit var mDialog : ProgressDialog
    //create the object for getting the fixtures
    val mFixtureListApiService by lazy {
        FixtureListApiService().loadFixturelist()
    }

    //the object from RXjava 2.0 to track the fetching activity
    var mDisposable: Disposable? = null

    //set broadcastReceiver to check internet connection to determine network call or access local data
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

        //get String from intent
        mRoomKey = intent.getStringExtra(MainViewHolder.ROOM_KEY)
        Log.d("mRoomKey", mRoomKey)

        //setup toolbar fancy stuff
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = mRoomKey


        //initialization
  //      mTemperatureValue = PrefHelper.getTemperature(applicationContext)
        mFixtureList = ArrayList()
        mAdapter = FixtureAdapter(mRoomKey, mFixtureList!!)

        //set recyclerview properties
        recyclerView_main.layoutManager = LinearLayoutManager(this)
        recyclerView_main!!.itemAnimator = DefaultItemAnimator()
        recyclerView_main!!.adapter = mAdapter

        loadLocalCache(false)
        attachPrefListener()
    }

    //function to get fixturelist of each room
    fun fetchFixturelist() {


        mDisposable = mFixtureListApiService.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { onRetrieveFixtureListStart() }
                .doOnTerminate { onRetrieveFixtureListFinish() }
                .subscribe({ result ->
                    onRetrieveFixtureListSuccess(result)
                }, { error ->
                        onRetrieveFixtureListError(error)
                 })
    }


private fun onRetrieveFixtureListStart(){
  //  UiHelper.showProgressDialog(applicationContext,R.string.message_loading_fixture,true)



//    loadingVisibility.value = View.VISIBLE
//    errorMessage.value = null
}

private fun onRetrieveFixtureListFinish(){
  //  loadingVisibility.value = View.GONE


}

private fun onRetrieveFixtureListSuccess(result: SmartRooms){

    //clean up fixture list
    mFixtureList!!.clear()

    // adding fixture to fixture list & persist in shared preference
    if (mRoomKey == "bedroom") {

        val x = result!!.rooms?.bedroom?.fixtures!!.size
        mFixtureList!!.addAll(result.rooms?.bedroom?.fixtures ?: mutableListOf())

        val fixtureValue = result.rooms?.bedroom?.fixtures!!.joinToString()
        //save fixture list
        StoreFixtureStatus(mRoomKey, fixtureValue, x)

    } else if (mRoomKey == "living-room") {

        val y = result!!.rooms?.livingroom?.fixtures!!.size
        mFixtureList!!.addAll(result.rooms?.livingroom?.fixtures ?: mutableListOf())
        val fixtureValue = result.rooms?.livingroom?.fixtures!!.joinToString()

        //save fixture status
        StoreFixtureStatus(mRoomKey, fixtureValue, y)

    } else if (mRoomKey == "kitchen") {

        val z = result.rooms?.kitchen?.fixtures!!.size
        mFixtureList!!.addAll(result.rooms?.kitchen?.fixtures ?: mutableListOf())
        val fixtureValue = result.rooms?.kitchen?.fixtures!!.joinToString()

        //save fixture list and status
        StoreFixtureStatus(mRoomKey, fixtureValue, z)
    }

   //  refreshing recycler view
                    runOnUiThread {
                        mAdapter!!.notifyDataSetChanged()
                    }
 }

private fun onRetrieveFixtureListError(error: Throwable){

                       error.printStackTrace()

}

    //Method to monitor change of temperature and control AC on/off
    fun attachPrefListener() {

        // setup listener for temperature
        val mPrefs = PrefHelper.getSharedPreferences(this)

        // Instance field for listener
        val mListener = SharedPreferences.OnSharedPreferenceChangeListener { mPrefs, key ->

            //when change in shared preference, load the data in shared preference again
            Log.d("Settings key changed: ", key)
            loadLocalCache(false)
        }
        mPrefs.registerOnSharedPreferenceChangeListener(mListener)

    }

    //Method to store fixture status if new fixture is retrieved from API, and set status to OFF at initial status
    private fun StoreFixtureStatus(mRoomKey: String, fixtureValue: String, j: Int) {

        //Save & update fixturelist everytime API is called.
        PrefHelper.storeKey(applicationContext, mRoomKey, fixtureValue)

        //Split the string to array list of fixture and save the status to OFF if status is null
        val result: List<String> = fixtureValue.split(",").map { it.trim() }
        for (i in 0 until j) {
            val fixtureKey = "${mRoomKey.toLowerCase()}_${result[i].toLowerCase()}"
            if (PrefHelper.containKey(applicationContext, fixtureKey) == false) {
                PrefHelper.storeKey(applicationContext, fixtureKey, "off")
            }
        }
    }


    //Method to display network status and get data from local
    private fun loadLocalCache(displayimage: Boolean) {

        //clean up fixture list
        mFixtureList!!.clear()

        // get fixturelist from shared preference
        val fixtureValue = PrefHelper.getKey(applicationContext, mRoomKey)

        //check is there any persistent data for the room
        if (fixtureValue != null) {

            //split string and display
            Log.d("GET_FIXTURELIST_TAG", fixtureValue)
            val result: List<String> = fixtureValue.split(",").map { it.trim() }
            mFixtureList!!.addAll(result)

        } else {

            //display alert image
            if (displayimage) imageview_nointernet.visibility = (VISIBLE) else
                imageview_nointernet.visibility = (INVISIBLE)
        }

        // refreshing recycler view
        runOnUiThread {
            mAdapter!!.notifyDataSetChanged()
        }

    }

    //Method to run when there is network connection
    private fun connected() {

         imageview_nointernet.visibility = (INVISIBLE)
         //get fixture list
        fetchFixturelist()

    }

    //Method to run when there is network connection
    private fun disconnected() {
        //show alert dialog
        UiHelper.showNetworkAlertDialog(this)

        loadLocalCache(true)

    }

    override fun onStart() {
        super.onStart()
        registerReceiver(mBroadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(mBroadcastReceiver)
    }

}
