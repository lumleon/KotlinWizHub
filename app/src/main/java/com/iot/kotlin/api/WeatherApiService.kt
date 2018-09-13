package com.iot.kotlin.api

import com.google.gson.GsonBuilder
import com.iot.kotlin.model.MetaWeather
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**  The Retrofit class to fetch Json from the API of Metaweather website as defined in the baseurl
 *   This class creates an instance of Retrofit
 *   It also defined a function as an observable object for the Metaweather class
 */

class WeatherApiService {

    val service: WeatherApiServiceInterface
    var baseUrl = "https://www.metaweather.com/api/location/"

    init {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

        val gson = GsonBuilder()
                .setLenient()
                .create()

        val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build()

        service = retrofit.create<WeatherApiServiceInterface>(WeatherApiServiceInterface::class.java)
    }

    fun loadTemp(): Observable<MetaWeather> {
        return service.getHKTemp()
    }
}