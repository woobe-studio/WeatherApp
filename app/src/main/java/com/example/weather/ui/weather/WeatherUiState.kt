package com.example.weather.ui.weather

import com.example.weather.model.Weather

data class WeatherUiState(
    val weather: Weather? = null,
    val isLoading: Boolean = false,
    val errorMessage: String = "",
)
