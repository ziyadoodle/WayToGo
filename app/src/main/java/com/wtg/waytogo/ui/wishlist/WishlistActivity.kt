package com.wtg.waytogo.ui.wishlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wtg.waytogo.R
import com.wtg.waytogo.databinding.ActivityWishlistBinding
import com.wtg.waytogo.ui.MainActivity
import com.wtg.waytogo.ui.explore.ExploreActivity

class WishlistActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWishlistBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWishlistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationView.selectedItemId = R.id.bottom_wishlist
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                    true
                }

                R.id.bottom_explore -> {
                    startActivity(Intent(this, ExploreActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                    true
                }

                R.id.bottom_wishlist -> true

                else -> false
            }
        }
    }
}