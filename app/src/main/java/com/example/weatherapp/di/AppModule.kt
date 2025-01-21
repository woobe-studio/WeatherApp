package com.example.weatherapp.di

import android.content.Context
import com.example.weatherapp.core.PreferencesHelper
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import com.example.weatherapp.data.repository.AuthRepositoryImpl
import com.example.weatherapp.domain.repository.AuthRepository

@Module
@InstallIn(ViewModelComponent::class)
class AppModule {

    @Provides
    fun provideAuthRepository(): AuthRepository = AuthRepositoryImpl(
        auth = Firebase.auth
    )

    @Provides
    fun providePreferencesHelper(@ApplicationContext context: Context): PreferencesHelper {
        return PreferencesHelper(context)
    }
}
