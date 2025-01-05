package com.example.weatherapp.data.repository

import com.example.weatherapp.data.model.Weather
import com.example.weatherapp.core.Result
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun getWeatherForecast(city: String): Flow<Result<Weather>>
}