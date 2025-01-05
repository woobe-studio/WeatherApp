package com.example.weatherapp.presentation.forgot_password.components

import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.weatherapp.R
import com.example.weatherapp.components.ActionIconButton

@Composable
fun ForgotPasswordTopBar(
    onArrowBackIconClick: () -> Unit
) {
    TopAppBar (
        title = {
            Text(
                text = stringResource(
                    id = R.string.forgot_password_screen_title
                )
            )
        },
        navigationIcon = {
            ActionIconButton(
                onActionIconButtonClick = onArrowBackIconClick,
                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                resourceId = R.string.navigate_back
            )
        }
    )
}