package com.iot.kotlin.api

import com.iot.kotlin.model.SmartRooms
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path


/**
 * This is the interface class that defines the path of the REST API call to the rooms
 */

interface FixtureListApiServiceInterface {

    // get the fixture list of each room
    @GET("rooms")
    fun getFixtureList(): Observable<SmartRooms>

    // get the on/off  status of fixture
    @GET("{room}/{fixture}/{onoff}")
    fun getFixtureStatus(@Path("room") room: String,
                         @Path("fixture") fixture: String,
                         @Path("onoff") onoff: String): Observable<Boolean>


}