package com.iot.kotlin.controller

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.iot.kotlin.api.WeatherApiService
import com.iot.kotlin.util.Constant.Companion.TEMPERATURE_THRESHOLD
import com.iot.kotlin.util.PrefHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * The object for getting current temperature from metaweather API
 * It uses Retrofit & RxJava to get the REST API.
 * The first record of the consolidated weather is the current temperature returned from the API
 */

class GetTemperatureController : AppCompatActivity() {

    companion object {

        //create the object for getting the HK temperature
        val hkTempApiService by lazy {
            WeatherApiService().loadTemp()
        }

        //the object from RXjava 2.0 to track the fetching activity
        var mDisposable: Disposable? = null

        //function to get temperature
        fun getCurrentTemp(context: Context, room: String, fixture: String) {

            mDisposable = hkTempApiService.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ result ->
                        //get the first array as current temperature
                        val mCurrentTemp = result.consolidatedWeather[0].theTemp
                        val mFixtureKey = room + "_" + fixture
                        val mFixtureStatus = PrefHelper.getKey(context, mFixtureKey)

                        //save to shared preference
                        PrefHelper.storeTemperature(context, String.format("%.2f", mCurrentTemp))
                        val mCurrentTemperatureValue = PrefHelper.getTemperature(context)!!.toDouble()
                        Log.d("GetTempTAG", mCurrentTemperatureValue.toString())
                        Log.d("FixtureKeyTAG", mFixtureKey)
                        Log.d("FixtureStatusTAG", mFixtureStatus)

                        if (mCurrentTemperatureValue > TEMPERATURE_THRESHOLD
                                && mFixtureStatus == "off") {
                            Log.d("getTemp", mCurrentTemperatureValue.toString())
                            TurnFixtureOnOffController.turnAcOnOffinBackground(context, room, fixture, "on")
                        } else if (mCurrentTemperatureValue <= TEMPERATURE_THRESHOLD
                                && mFixtureStatus == "on") {
                            TurnFixtureOnOffController.turnAcOnOffinBackground(context, room, fixture, "off")
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