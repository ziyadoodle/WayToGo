package com.wtg.waytogo.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.google.gson.Gson
import com.wtg.waytogo.data.PlacePagingSource
import com.wtg.waytogo.data.pref.ResultState
import com.wtg.waytogo.data.pref.UserModel
import com.wtg.waytogo.data.pref.UserPreferences
import com.wtg.waytogo.data.response.PlaceItem
import com.wtg.waytogo.data.response.SignInResponse
import com.wtg.waytogo.data.response.SignUpResponse
import com.wtg.waytogo.data.source.ApiService
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userPreferences: UserPreferences
) {
    private val _isLoading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _isLoading

    fun signIn(email: String, password: String) = liveData {
        emit(ResultState.Loading)

        try {
            val success = apiService.signIn(email, password)
            emit(ResultState.Success(success))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val error = Gson().fromJson(errorBody, SignInResponse::class.java)
            emit(error.message?.let { ResultState.Error(it) })
        }
    }

    fun signUp(username: String, email: String, password: String) = liveData {
        emit(ResultState.Loading)

        try {
            val success = apiService.signUp(username, email, password)
            emit(ResultState.Success(success))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val error = Gson().fromJson(errorBody, SignUpResponse::class.java)
            emit(error.message?.let { ResultState.Error(it) })
        }
    }

    fun getPlaces(token: String): LiveData<PagingData<PlaceItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                PlacePagingSource(apiService, userPreferences)
            }
        ).liveData
    }

//    fun getDetailPlace(token: String, id: String): LiveData<>

    suspend fun saveSession(user: UserModel) {
        userPreferences.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreferences.getSession()
    }

    suspend fun logout() {
        userPreferences.logout()
    }

    companion object {
        @Volatile
        private var INSTANCE: UserRepository? = null

        fun getInstance(
            apiService: ApiService,
            userPreferences: UserPreferences
        ): UserRepository = INSTANCE ?: synchronized(this) {
            INSTANCE ?: UserRepository(apiService, userPreferences)
        }.also { INSTANCE = it }
    }
}