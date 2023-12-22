package com.wtg.waytogo.di

import android.content.Context
import com.wtg.waytogo.data.pref.UserPreferences
import com.wtg.waytogo.data.pref.dataStore
import com.wtg.waytogo.data.repository.UserRepository
import com.wtg.waytogo.data.source.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {

    fun provideRepository(context: Context): UserRepository {
        val preferences = UserPreferences.getInstance(context.dataStore)
        val user = runBlocking { preferences.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)

        return UserRepository.getInstance(apiService, preferences)
    }

}