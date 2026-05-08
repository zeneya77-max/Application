package com.example.application

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.application.databinding.PaidSanctionsScreenBinding
import com.google.android.material.tabs.TabLayout

class PaidSanctionsActivity : AppCompatActivity() {
    private lateinit var binding: PaidSanctionsScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PaidSanctionsScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Siguraduhin na ang violet line ay lilipat sa "PAID" (index 2)
        binding.filterTabs.post {
            binding.filterTabs.getTabAt(2)?.select()
        }

        binding.filterTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        startActivity(Intent(this@PaidSanctionsActivity, MySanctionsActivity::class.java))
                        finish()
                        overridePendingTransition(0, 0)
                    }
                    1 -> {
                        startActivity(Intent(this@PaidSanctionsActivity, UnpaidSanctionsActivity::class.java))
                        finish()
                        overridePendingTransition(0, 0)
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

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
