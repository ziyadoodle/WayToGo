package com.wtg.waytogo.ui.profile

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.wtg.waytogo.databinding.ActivityProfileBinding
import com.wtg.waytogo.ui.ViewModelFactory

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    private val viewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this) { user ->
            binding.tvUsername.text = user.username
            binding.tvEmail.text = user.email
        }

        binding.signOutButton.setOnClickListener {
            viewModel.logout()
            finish()
        }

        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }
    }
}