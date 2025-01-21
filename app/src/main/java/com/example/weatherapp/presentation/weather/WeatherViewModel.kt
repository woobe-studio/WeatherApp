package com.example.weatherapp.presentation.weather

import com.example.weatherapp.core.Result
import com.example.weatherapp.core.PreferencesHelper
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.core.DEFAULT_WEATHER_DESTINATION
import com.example.weatherapp.presentation.weather.SearchWidgetState
import com.example.weatherapp.presentation.weather.WeatherUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val preferencesHelper: PreferencesHelper // Inject PreferencesHelper
) : ViewModel() {

    private val _uiState: MutableStateFlow<WeatherUiState> =
        MutableStateFlow(WeatherUiState(isLoading = true))
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    private val _searchWidgetState: MutableState<SearchWidgetState> =
        mutableStateOf(value = SearchWidgetState.CLOSED)
    val searchWidgetState: State<SearchWidgetState> = _searchWidgetState

    private val _searchTextState: MutableState<String> = mutableStateOf(value = "")
    val searchTextState: State<String> = _searchTextState

    fun updateSearchWidgetState(newValue: SearchWidgetState) {
        _searchWidgetState.value = newValue
    }

    fun updateSearchTextState(newValue: String) {
        _searchTextState.value = newValue
    }

    init {
        // Load the saved city or use default if not found
        val savedCity = preferencesHelper.getCity() ?: DEFAULT_WEATHER_DESTINATION
        getWeather(savedCity)
    }

    fun getWeather(city: String = DEFAULT_WEATHER_DESTINATION) {
        // Save the city to preferences
        saveCityToPreferences(city)
        repository.getWeatherForecast(city).map { result ->
            when (result) {
                is Result.Success -> {
                    _uiState.value = WeatherUiState(weather = result.data)
                }

                is Result.Error -> {
                    _uiState.value = WeatherUiState(errorMessage = result.errorMessage)
                }

                Result.Loading -> {
                    _uiState.value = WeatherUiState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun saveCityToPreferences(city: String) {
        preferencesHelper.saveCity(city)
    }
}
