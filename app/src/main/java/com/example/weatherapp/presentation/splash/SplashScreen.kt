package com.example.weatherapp.presentation.splash

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherapp.navigation.Route
import com.example.weatherapp.navigation.Route.Profile
import com.example.weatherapp.navigation.Route.SignIn

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    navigateAndClear: (Route) -> Unit
) {
    if (viewModel.isUserSignedOut) {
        navigateAndClear(SignIn)
    } else {
        navigateAndClear(Profile)
    }
}