package com.wtg.waytogo.ui.signUp

import androidx.lifecycle.ViewModel
import com.wtg.waytogo.data.repository.UserRepository

class SignUpViewModel(private val repository: UserRepository) : ViewModel() {

    fun signUp(username: String, email: String, password: String) =
        repository.signUp(username, email, password)

}