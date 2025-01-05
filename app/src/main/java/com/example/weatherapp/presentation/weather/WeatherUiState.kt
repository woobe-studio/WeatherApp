package com.example.weatherapp.presentation.weather

import com.example.weatherapp.data.model.Weather

data class WeatherUiState(
    val weather: Weather? = null,
    val isLoading: Boolean = false,
    val errorMessage: String = "",
)
