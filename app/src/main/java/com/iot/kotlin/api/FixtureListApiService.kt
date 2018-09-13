package com.iot.kotlin.api

import com.google.gson.GsonBuilder
import com.iot.kotlin.model.SmartRooms
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**  The Retrofit class to fetch Json from the API of rooms， which shows the  corresponding fixtures
 *   in each room as defined in the baseurl
 *   This class creates an instance of Retrofit
 *   It also defined a function as an observable object for the smartroom class
 */

class FixtureListApiService {

    val mService: FixtureListApiServiceInterface
    var mBaseUrl = "http://private-1e863-house4591.apiary-mock.com/"

    init {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

        val gson = GsonBuilder()
                .setLenient()
                .toString()
        //   .create()

        val retrofit = Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build()

        mService = retrofit.create<FixtureListApiServiceInterface>(FixtureListApiServiceInterface::class.java)
    }

    fun loadFixturelist(): Observable<SmartRooms> {
        return mService.getFixtureList()
    }


    fun loadFixtureStatus(room: String, fixture: String, onoff: String): Observable<Boolean> {
        return mService.getFixtureStatus(room, fixture, onoff)
    }
}


