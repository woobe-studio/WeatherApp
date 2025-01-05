package com.example.weatherapp.presentation.forgot_password

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import com.example.weatherapp.core.launchCatching
import com.example.weatherapp.domain.model.Response
import com.example.weatherapp.domain.model.Response.Loading
import com.example.weatherapp.domain.repository.AuthRepository
import javax.inject.Inject

typealias SendPasswordResetEmailResponse = Response<Unit>

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val repo: AuthRepository
): ViewModel() {
    var sendPasswordResetEmailResponse by mutableStateOf<SendPasswordResetEmailResponse>(Loading)

    fun sendPasswordResetEmail(email: String) = viewModelScope.launch {
        sendPasswordResetEmailResponse = launchCatching {
            repo.sendPasswordResetEmail(email)
        }
    }
}