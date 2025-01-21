package com.example.weatherapp.core

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class PreferencesHelper @Inject constructor(private val context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("weather_app_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_CITY = "Warsaw"
    }

    fun saveCity(city: String) {
        sharedPreferences.edit().putString(KEY_CITY, city).apply()
    }

    fun getCity(): String? {
        return sharedPreferences.getString(KEY_CITY, null)
    }
}
