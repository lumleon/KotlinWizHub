package com.iot.kotlin.api

import com.iot.kotlin.model.MetaWeather
import io.reactivex.Observable
import retrofit2.http.GET

/**
 * This is the interface class that defines the path for the REST API call to the metaweather
 * the value 2165252 is the string for Hong Kong location
 * It implements Rxjava's observable for the metaweather class
 */

interface WeatherApiServiceInterface {

    // get the weather of Hong Kong
    @GET("2165352")
    fun getHKTemp(): Observable<MetaWeather>

}