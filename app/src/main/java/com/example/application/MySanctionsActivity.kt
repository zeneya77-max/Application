package com.example.application

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.application.databinding.MySanctionsScreenBinding
import com.google.android.material.tabs.TabLayout

class MySanctionsActivity : AppCompatActivity() {
    private lateinit var binding: MySanctionsScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MySanctionsScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set "All" tab as selected
        binding.filterTabs.getTabAt(0)?.select()

        binding.filterTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    1 -> {
                        startActivity(Intent(this@MySanctionsActivity, UnpaidSanctionsActivity::class.java))
                        finish()
                        overridePendingTransition(0, 0)
                    }
                    2 -> {
                        startActivity(Intent(this@MySanctionsActivity, PaidSanctionsActivity::class.java))
                        finish()
                        overridePendingTransition(0, 0)
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        // Pay Now Button
        binding.btnPayNow.setOnClickListener {
            startActivity(Intent(this, PayNowActivity::class.java))
        }

        // Navigation Setup
        binding.btnNavigation.selectedItemId = R.id.nav_sanctions
        binding.btnNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, HomepageActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_sanctions -> true
                R.id.nav_notifications -> {
                    startActivity(Intent(this, NotificationsActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }
}
