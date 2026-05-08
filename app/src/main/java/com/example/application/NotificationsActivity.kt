package com.example.application

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.application.databinding.NotificationsScreenBinding

class NotificationsActivity : AppCompatActivity() {
    private lateinit var binding: NotificationsScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = NotificationsScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNavigation.selectedItemId = R.id.nav_notifications
        binding.btnNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, HomepageActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_sanctions -> {
                    startActivity(Intent(this, MySanctionsActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_notifications -> true
                else -> false
            }
        }
    }
}
