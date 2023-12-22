package com.wtg.waytogo.ui.signUp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.wtg.waytogo.data.pref.ResultState
import com.wtg.waytogo.databinding.ActivitySignUpBinding
import com.wtg.waytogo.ui.ViewModelFactory

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    private val viewModel by viewModels<SignUpViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoading(false)
        setupAction()
    }

    private fun setupAction() {
        binding.signUpButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            viewModel.signUp(username, email, password).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is ResultState.Loading -> {
                            showLoading(true)
                        }

                        is ResultState.Success -> {
                            result.data.message?.let {
                                Toast.makeText(this, result.data.message, Toast.LENGTH_SHORT).show()
                            }
                            finish()
                        }

                        is ResultState.Error -> {
                            showLoading(false)
                            Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.signUpButton.isEnabled = !isLoading
    }

}