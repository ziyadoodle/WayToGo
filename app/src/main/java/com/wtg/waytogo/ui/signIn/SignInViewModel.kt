package com.wtg.waytogo.ui.signIn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wtg.waytogo.data.pref.UserModel
import com.wtg.waytogo.data.repository.UserRepository
import kotlinx.coroutines.launch

class SignInViewModel(private val repository: UserRepository) : ViewModel() {

    fun signInUser(email: String, password: String) = repository.signIn(email, password)

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

}