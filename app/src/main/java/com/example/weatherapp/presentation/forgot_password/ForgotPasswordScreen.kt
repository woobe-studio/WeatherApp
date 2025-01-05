package com.example.weatherapp.presentation.forgot_password

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherapp.R
import com.example.weatherapp.components.LoadingIndicator
import com.example.weatherapp.core.printError
import com.example.weatherapp.core.showToastError
import com.example.weatherapp.core.showToastMessage
import com.example.weatherapp.domain.model.Response.Failure
import com.example.weatherapp.domain.model.Response.Loading
import com.example.weatherapp.domain.model.Response.Success
import com.example.weatherapp.presentation.forgot_password.components.ForgotPasswordContent
import com.example.weatherapp.presentation.forgot_password.components.ForgotPasswordTopBar

@Composable
fun ForgotPasswordScreen(
    viewModel: ForgotPasswordViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val context = LocalContext.current
    var sendingPasswordResetEmail by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            ForgotPasswordTopBar(
                onArrowBackIconClick = navigateBack
            )
        }
    ) { innerPadding ->
        ForgotPasswordContent(
            innerPadding = innerPadding,
            onSendingPasswordResetEmail = { email ->
                viewModel.sendPasswordResetEmail(email)
                sendingPasswordResetEmail = true
            },
            sendingPasswordResetEmail = sendingPasswordResetEmail
        )
    }

    if (sendingPasswordResetEmail) {
        when(val sendPasswordResetEmailResponse = viewModel.sendPasswordResetEmailResponse) {
            is Loading -> LoadingIndicator()
            is Success -> {
                showToastMessage(context, R.string.reset_password_message)
                navigateBack()
                sendingPasswordResetEmail = false
            }
            is Failure -> sendPasswordResetEmailResponse.e.let { e ->
                printError(e)
                showToastError(context, e)
                sendingPasswordResetEmail = false
            }
        }
    }
}