package com.wtg.waytogo.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wtg.waytogo.R
import com.wtg.waytogo.databinding.ActivityMainBinding
import com.wtg.waytogo.ui.explore.ExploreActivity
import com.wtg.waytogo.ui.profile.ProfileActivity
import com.wtg.waytogo.ui.welcome.WelcomeActivity
import com.wtg.waytogo.ui.wishlist.WishlistActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.rvPopular
        adapter = MainAdapter()

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else {
                binding.tvUsername.text = user.username
                val adapter = MainAdapter()
                lifecycleScope.launch {
                    adapter.loadStateFlow.collectLatest { loadStates ->
                        binding.progressBar.isVisible = loadStates.refresh is LoadState.Loading
                    }
                }

                binding.rvPopular.adapter = adapter

                viewModel.getPlace(user.token).observe(this) { place ->
                    if (place != null) {
                        adapter.submitData(lifecycle, place)
                    }
                }
            }
        }

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.profile -> {
                    val profileIntent = Intent(this, ProfileActivity::class.java)
                    startActivity(profileIntent)
                    true
                }

                else -> false
            }
        }

        binding.bottomNavigationView.selectedItemId = R.id.bottom_home
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_home -> true

                R.id.bottom_explore -> {
                    startActivity(Intent(this, ExploreActivity::class.java))
                    @Suppress("DEPRECATION")
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                    true
                }

                R.id.bottom_wishlist -> {
                    startActivity(Intent(this, WishlistActivity::class.java))
                    @Suppress("DEPRECATION")
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                    true
                }

                else -> false
            }
        }
    }
}
