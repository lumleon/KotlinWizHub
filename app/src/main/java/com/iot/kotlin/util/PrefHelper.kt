package com.iot.kotlin.util

import android.content.Context
import android.content.SharedPreferences


/**
 * The class using shared preference to act as data persistence for Fixture status, temperature  in shared preferences to
 */
class PrefHelper {
    companion object {

        //return the default shared preference
        fun getSharedPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences("APP_PREF", Context.MODE_PRIVATE)

        }

        //method to store key and the value
        fun storeKey(context: Context, Key: String, value: String) {
            val editor = getSharedPreferences(context).edit()
            editor.putString(Key, value)
            editor.apply()
        }

        //method to get value of key
        fun getKey(context: Context, fixtureKey: String): String? {
            return getSharedPreferences(context).getString(fixtureKey, null)
        }

        //method to get the temperature
        fun getTemperature(context: Context): String? {
            return getSharedPreferences(context).getString("theTemp", "30")
        }

        //method to store the temperature
        fun storeTemperature(context: Context, value: String) {
            val editor = getSharedPreferences(context).edit()
            editor.putString("theTemp", value)
            editor.apply()

        }

        //method to check is the key exits
        fun containKey(context: Context, fixtureKey: String): Boolean? {

            return getSharedPreferences(context).contains(fixtureKey)
        }

    }
}