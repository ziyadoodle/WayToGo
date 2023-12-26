package com.wtg.waytogo.data.pref

data class UserModel(
    val email: String,
    val token: String,
    val username: String,
    val isLogin: Boolean = false
)