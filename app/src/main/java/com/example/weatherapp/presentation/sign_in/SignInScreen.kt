package com.example.weatherapp.presentation.sign_in

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherapp.components.LoadingIndicator
import com.example.weatherapp.core.printError
import com.example.weatherapp.core.showToastError
import com.example.weatherapp.domain.model.Response.Failure
import com.example.weatherapp.domain.model.Response.Loading
import com.example.weatherapp.domain.model.Response.Success
import com.example.weatherapp.navigation.Route
import com.example.weatherapp.navigation.Route.ForgotPassword
import com.example.weatherapp.navigation.Route.Profile
import com.example.weatherapp.navigation.Route.SignUp
import com.example.weatherapp.presentation.sign_in.components.SignInContent
import com.example.weatherapp.presentation.sign_in.components.SignInTopBar

@Composable
fun SignInScreen(
    viewModel: SignInViewModel = hiltViewModel(),
    navigate: (Route) -> Unit,
    navigateAndClear: (Route) -> Unit
) {
    val context = LocalContext.current
    var signingIn by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            SignInTopBar()
        }
    ) { innerPadding ->
        SignInContent(
            innerPadding = innerPadding,
            onSigningIn = { email, password ->
                viewModel.signInWithEmailAndPassword(email, password)
                signingIn = true
            },
            signingIn = signingIn,
            onForgotPasswordTextClick = {
                navigate(ForgotPassword)
            },
            onSignUpTextClick = {
                navigate(SignUp)
            }
        )
    }

    if (signingIn) {
        when(val signInResponse = viewModel.signInResponse) {
            is Loading -> LoadingIndicator()
            is Success -> {
                navigateAndClear(Profile)
                signingIn = false
            }
            is Failure -> signInResponse.e.let{ e ->
                printError(e)
                showToastError(context, e)
                signingIn = false
            }
        }
    }
}