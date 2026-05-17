package com.example.application

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_screen)

        val sharedPref = getSharedPreferences("SancSeePrefs", MODE_PRIVATE)
        findViewById<TextView>(R.id.tvProfileName).text = sharedPref.getString("saved_full_name", "Student")

        findViewById<BottomNavigationView>(R.id.btnNavigation).selectedItemId = R.id.nav_profile
        findViewById<BottomNavigationView>(R.id.btnNavigation).setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, HomepageActivity::class.java))
                    finish()
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.nav_sanctions -> {
                    startActivity(Intent(this, MySanctionsActivity::class.java))
                    finish()
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.nav_notifications -> {
                    startActivity(Intent(this, StudentNotificationsActivity::class.java))
                    finish()
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.nav_profile -> true
                else -> true
            }
        }

        findViewById<TextView>(R.id.btnLogout).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}
