package com.iot.kotlin.controller

import android.content.Context
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.iot.kotlin.api.FixtureListApiService
import com.iot.kotlin.util.PrefHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * The object for getting current temperature from metaweather API
 * It uses Retrofit & RxJava to get the REST API.
 * The first record of the consolidated weather is the current temperature returned from the API
 */

class TurnFixtureOnOffController : AppCompatActivity() {

    companion object {

        //the object from RXjava 2.0 to track the fetching activity
        var mDisposable: Disposable? = null

        //method to handle turn AC on/off
        fun turnAcOnOffinBackground(context: Context, room: String, fixture: String, onOff: String) {

            val mFixtureStatusApiService = FixtureListApiService().loadFixtureStatus(room, fixture, onOff)
            val mAcKey = room + "_" + fixture

            mDisposable = mFixtureStatusApiService
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ result ->

                        // adding fixture to fixture list & persist in shared preference
                        val status = result

                        if (status) {
                            //update data locally and to be show in fixture list when user open the app
                            PrefHelper.storeKey(context, mAcKey, onOff)
                        }

                    }, { error ->
                        error.printStackTrace()
                    })
        }

        //method to handle the display of temperature and on/off status of fixture
        fun turnFixtureOnOffinUI(view: View, room: String, fixture: String, onOff: String) {

            val mFixtureStatusApiService = FixtureListApiService().loadFixtureStatus(room, fixture, onOff)
            val mDisposable: Disposable
            val mFixtureKey = room + "_" + fixture


            mDisposable = mFixtureStatusApiService
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ result ->

                        // adding fixture to fixture list & persist in shared preference
                        val status = result

                        if (status) {

                            PrefHelper.storeKey(view.context, mFixtureKey, onOff)
                            Snackbar.make(
                                    view, // Parent view
                                    "Saving $fixture status to $onOff!", // Message to show
                                    Snackbar.LENGTH_SHORT // How long to display the message.
                            ).show()

                        }

                    }, { error ->
                        error.printStackTrace()
                    })


        }


    }

    override fun onPause() {
        super.onPause()
        mDisposable?.dispose()

    }

    override fun onDestroy() {
        super.onDestroy()
        mDisposable?.dispose()

    }

}