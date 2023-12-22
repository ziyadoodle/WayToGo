package com.wtg.waytogo.ui.welcome

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.wtg.waytogo.R
import com.wtg.waytogo.databinding.ActivityWelcomeBinding
import com.wtg.waytogo.ui.signIn.SignInActivity
import com.wtg.waytogo.ui.signUp.SignUpActivity

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.descTextView.text = Html.fromHtml("<font color=${Color.WHITE}>Destination for your next</font><font color=#0A8E86> vacation</font>")

        binding.signInButton.setOnClickListener {
            val signInIntent = Intent(this, SignInActivity::class.java)
            startActivity(signInIntent)
        }

        binding.signUpButton.setOnClickListener {
            val signUpIntent = Intent(this, SignUpActivity::class.java)
            startActivity(signUpIntent)
        }
    }
}