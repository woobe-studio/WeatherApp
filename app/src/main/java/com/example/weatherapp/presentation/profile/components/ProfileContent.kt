package com.example.weatherapp.presentation.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileContent(
    innerPadding: PaddingValues,
    navigateToWeather: () -> Unit,
    isDarkTheme: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Welcome to the Profile Screen",
            fontSize = 24.sp,
            color = if (isDarkTheme) Color.White else Color.Black // Dynamic text color
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = navigateToWeather) {
            Text(text = "Go to Weather")
        }
    }
}
