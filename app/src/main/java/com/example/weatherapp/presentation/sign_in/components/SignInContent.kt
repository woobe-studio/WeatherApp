package com.example.weatherapp.presentation.sign_in.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.R
import com.example.weatherapp.components.ActionButton
import com.example.weatherapp.components.ActionText
import com.example.weatherapp.components.EmailField
import com.example.weatherapp.components.PasswordField
import com.example.weatherapp.core.EMPTY_STRING

const val VERTICAL_DIVIDER = "|"

@Composable
fun SignInContent(
    innerPadding: PaddingValues,
    onSigningIn: (String, String) -> Unit,
    signingIn: Boolean,
    onForgotPasswordTextClick: () -> Unit,
    onSignUpTextClick: () -> Unit
) {
    var email by rememberSaveable(
        stateSaver = TextFieldValue.Saver
    ) { mutableStateOf(TextFieldValue(EMPTY_STRING)) }
    var password by rememberSaveable(
        stateSaver = TextFieldValue.Saver
    ) { mutableStateOf(TextFieldValue(EMPTY_STRING)) }
    val keyboard = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier.fillMaxSize().padding(innerPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmailField(
            email = email,
            onEmailValueChange = { newEmail ->
                email = newEmail
            }
        )
        Spacer(
            modifier = Modifier.height(8.dp)
        )
        PasswordField(
            password = password,
            onPasswordValueChange = { newPassword ->
                password = newPassword
            }
        )
        Spacer(
            modifier = Modifier.height(8.dp)
        )
        ActionButton(
            onActionButtonClick = {
                onSigningIn(email.text, password.text)
                keyboard?.hide()
            },
            enabled = !signingIn,
            resourceId = R.string.sign_in_button
        )
        Row {
            ActionText(
                onActionTextClick = onForgotPasswordTextClick,
                resourceId = R.string.forgot_password
            )
            Text(
                modifier = Modifier.padding(
                    start = 4.dp,
                    end = 4.dp
                ),
                text = VERTICAL_DIVIDER,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            ActionText(
                onActionTextClick = onSignUpTextClick,
                resourceId = R.string.sign_up
            )
        }
    }
}