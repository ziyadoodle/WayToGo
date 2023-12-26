package com.wtg.waytogo.ui.explore

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
import com.wtg.waytogo.databinding.ActivityExploreBinding
import com.wtg.waytogo.ui.LoadingStateAdapter
import com.wtg.waytogo.ui.MainActivity
import com.wtg.waytogo.ui.ViewModelFactory
import com.wtg.waytogo.ui.welcome.WelcomeActivity
import com.wtg.waytogo.ui.wishlist.WishlistActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ExploreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExploreBinding

    private val viewModel by viewModels<ExploreViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ExploreAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExploreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.rvPlace
        adapter = ExploreAdapter()

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else {
                val adapter = ExploreAdapter()
                lifecycleScope.launch {
                    adapter.loadStateFlow.collectLatest { loadStates ->
                        binding.progressBar.isVisible = loadStates.refresh is LoadState.Loading
                    }
                }

                binding.rvPlace.adapter = adapter.withLoadStateFooter(
                    footer = LoadingStateAdapter { adapter.retry() }
                )

                viewModel.getPlace(user.token).observe(this) { place ->
                    if (place != null) {
                        adapter.submitData(lifecycle, place)
                    }
                }
            }
        }

        binding.bottomNavigationView.selectedItemId = R.id.bottom_explore
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                    true
                }

                R.id.bottom_explore -> true

                R.id.bottom_wishlist -> {
                    startActivity(Intent(this, WishlistActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                    true
                }

                else -> false
            }
        }
    }
}