package com.wtg.waytogo.ui.signIn

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.wtg.waytogo.data.pref.ResultState
import com.wtg.waytogo.data.pref.UserModel
import com.wtg.waytogo.databinding.ActivitySignInBinding
import com.wtg.waytogo.ui.MainActivity
import com.wtg.waytogo.ui.ViewModelFactory

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding

    private val viewModel by viewModels<SignInViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoading(false)
        setUpAction()
    }

    private fun setUpAction() {
        binding.signInButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            viewModel.signInUser(email, password).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is ResultState.Loading -> {
                            showLoading(true)
                        }

                        is ResultState.Success -> {
                            result.data.message.let {
                                Toast.makeText(this, result.data.message, Toast.LENGTH_SHORT).show()
                                viewModel.saveSession(
                                    UserModel(
                                        email,
                                        "${result.data.token}",
                                        result.data.user?.displayName!!
                                    )
                                )
                                showLoading(false)
                            }
                            AlertDialog.Builder(this).apply {
                                setTitle("Yeay!")
                                setMessage("Anda berhasil login.")
                                setPositiveButton("Lanjut") { _, _ ->
                                    val intent = Intent(context, MainActivity::class.java)
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                    finish()
                                }
                                create()
                                show()
                            }
                        }

                        is ResultState.Error -> {
                            Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                            showLoading(false)
                        }
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.signInButton.isEnabled = !isLoading
    }

}