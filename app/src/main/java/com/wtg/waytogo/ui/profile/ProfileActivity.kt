package com.wtg.waytogo.ui.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.wtg.waytogo.R
import com.wtg.waytogo.databinding.ActivityProfileBinding
import com.wtg.waytogo.ui.MainActivity
import com.wtg.waytogo.ui.ViewModelFactory
import com.wtg.waytogo.ui.explore.ExploreActivity

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    private val viewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signOutButton.setOnClickListener {
            viewModel.logout()
            finish()
        }

        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }
    }
}