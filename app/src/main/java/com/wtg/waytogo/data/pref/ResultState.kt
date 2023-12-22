package com.wtg.waytogo.data.pref

sealed class ResultState<out T> private constructor() {

    data class Success<out T>(val data: T) : ResultState<T>()

    data class Error(val error: String) : ResultState<Nothing>()

    object Loading : ResultState<Nothing>()

}