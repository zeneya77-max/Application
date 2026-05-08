package com.example.application

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val logoImageView = findViewById<ImageView>(R.id.logoImageView)
        val appNameTextView = findViewById<TextView>(R.id.appNameTextView)

        // Load animations
        val logoAnim = AnimationUtils.loadAnimation(this, R.anim.splash_animation)
        val textAnim = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        textAnim.duration = 1000

        // Start logo animation
        logoImageView.startAnimation(logoAnim)

        // Show and animate text after a short delay
        Handler(Looper.getMainLooper()).postDelayed({
            appNameTextView.visibility = View.VISIBLE
            appNameTextView.startAnimation(textAnim)
        }, 800)

        // Transition to MainActivity
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }, 3000)
    }
}