package com.example.weatherapp.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarResult
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import com.example.weatherapp.R
import com.example.weatherapp.components.LoadingIndicator
import com.example.weatherapp.presentation.profile.components.ProfileTopBar
import com.example.weatherapp.core.printError
import com.example.weatherapp.core.showToastError
import com.example.weatherapp.core.showToastMessage
import com.example.weatherapp.domain.model.Response.*
import com.example.weatherapp.navigation.Route
import com.example.weatherapp.presentation.profile.components.ProfileContent
import com.example.weatherapp.presentation.profile.components.VerifyEmailContent

const val SIGN_OUT_ACTION_LABEL = "Sign out?"
const val DELETE_USER_MESSAGE = "You need to re-authenticate before deleting the user."
const val SENSITIVE_OPERATION_MESSAGE = "This operation is sensitive and requires recent authentication. Log in again before retrying this request."

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    navigateAndClear: (Route) -> Unit,
    navigateToWeather: () -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var isEmailVerified by remember { mutableStateOf(viewModel.isEmailVerified) }
    var reloadingUser by remember { mutableStateOf(false) }
    var deletingUser by remember { mutableStateOf(false) }
    var isDarkTheme by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getAuthState(
            navigateToSignInScreen = {
                navigateAndClear(Route.SignIn)
            }
        )
    }

    Scaffold(
        topBar = {
            ProfileTopBar(
                signOut = {
                    viewModel.signOut()
                },
                deleteUser = {
                    viewModel.deleteUser()
                    deletingUser = true
                },
                isDarkTheme = isDarkTheme,
                toggleTheme = { isDarkTheme = !isDarkTheme }
            )
        },
        scaffoldState = scaffoldState
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(if (isDarkTheme) Color.Black else Color.White) // Dynamic background color
        ) {
            if (isEmailVerified) {
                ProfileContent(
                    innerPadding = innerPadding,
                    navigateToWeather = navigateToWeather,
                    isDarkTheme = isDarkTheme
                )
            } else {
                VerifyEmailContent(
                    innerPadding = innerPadding,
                    reloadUser = {
                        viewModel.reloadUser()
                        reloadingUser = true
                    }
                )
            }
        }
    }

    if (reloadingUser) {
        when (val reloadUserResponse = viewModel.reloadUserResponse) {
            is Loading -> LoadingIndicator()
            is Success -> {
                if (viewModel.isEmailVerified) {
                    isEmailVerified = true
                } else {
                    showToastMessage(context, R.string.email_not_verified_message)
                }
                reloadingUser = false
            }
            is Failure -> reloadUserResponse.e.let { e ->
                printError(e)
                showToastError(context, e)
                reloadingUser = false
            }
        }
    }

    fun showDeleteUserMessage() = coroutineScope.launch {
        val result = scaffoldState.snackbarHostState.showSnackbar(
            message = DELETE_USER_MESSAGE,
            actionLabel = SIGN_OUT_ACTION_LABEL
        )
        if (result == SnackbarResult.ActionPerformed) {
            viewModel.signOut()
        }
    }

    if (deletingUser) {
        when (val deleteUserResponse = viewModel.deleteUserResponse) {
            is Loading -> LoadingIndicator()
            is Success -> {
                showToastMessage(context, R.string.user_deleted_message)
                deletingUser = false
            }
            is Failure -> deleteUserResponse.e.let { e ->
                printError(e)
                if (e.message == SENSITIVE_OPERATION_MESSAGE) {
                    showDeleteUserMessage()
                }
                deletingUser = false
            }
        }
    }
}
