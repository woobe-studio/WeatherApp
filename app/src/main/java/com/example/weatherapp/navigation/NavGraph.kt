package com.example.weatherapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.weatherapp.navigation.Route.ForgotPassword
import com.example.weatherapp.navigation.Route.Profile
import com.example.weatherapp.navigation.Route.SignIn
import com.example.weatherapp.navigation.Route.SignUp
import com.example.weatherapp.navigation.Route.Splash
import com.example.weatherapp.presentation.forgot_password.ForgotPasswordScreen
import com.example.weatherapp.presentation.profile.ProfileScreen
import com.example.weatherapp.presentation.sign_in.SignInScreen
import com.example.weatherapp.presentation.sign_up.SignUpScreen
import com.example.weatherapp.presentation.splash.SplashScreen

import com.example.weatherapp.presentation.weather.WeatherScreen

@Composable
fun NavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Splash
    ) {
        composable<Splash>  {
            SplashScreen(
                navigateAndClear = navController::navigateAndClear
            )
        }
        composable<SignIn>  {
            SignInScreen(
                navigate = navController::navigate,
                navigateAndClear = navController::navigateAndClear
            )
        }
        composable<ForgotPassword> {
            ForgotPasswordScreen(
                navigateBack = navController::navigateUp
            )
        }
        composable<SignUp> {
            SignUpScreen(
                navigateBack = navController::navigateUp,
                navigateAndClear = navController::navigateAndClear
            )
        }
        composable<Profile> {
            ProfileScreen(
                navigateAndClear = navController::navigateAndClear,
                navigateToWeather = { navController.navigate(Route.Weather) }
            )
        }
        composable<Route.Weather> {
            WeatherScreen(
                navigateToProfile = { navController.navigate(Route.Profile) }
            )
        }

    }
}

fun NavHostController.navigateAndClear(route: Route) = navigate(route) {
    popUpTo(graph.startDestinationId) {
        inclusive = true
    }
    graph.setStartDestination(route)
}