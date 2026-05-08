package com.example.application

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.application.databinding.UnpaidSanctionsScreenBinding
import com.google.android.material.tabs.TabLayout

class UnpaidSanctionsActivity : AppCompatActivity() {
    private lateinit var binding: UnpaidSanctionsScreenBinding
    private var totalSelectedAmount = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UnpaidSanctionsScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize display
        updateTotalDisplay()

        // Set up CheckBox listeners
        binding.cbItem1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) totalSelectedAmount += 18.0 else totalSelectedAmount -= 18.0
            updateTotalDisplay()
        }

        binding.cbItem2.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) totalSelectedAmount += 18.0 else totalSelectedAmount -= 18.0
            updateTotalDisplay()
        }

        // Siguraduhin na ang violet line ay lilipat sa "UNPAID" (index 1)
        binding.filterTabs.post {
            binding.filterTabs.getTabAt(1)?.select()
        }

        binding.filterTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        startActivity(Intent(this@UnpaidSanctionsActivity, MySanctionsActivity::class.java))
                        finish()
                        overridePendingTransition(0, 0)
                    }
                    2 -> {
                        startActivity(Intent(this@UnpaidSanctionsActivity, PaidSanctionsActivity::class.java))
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
            if (totalSelectedAmount > 0) {
                val intent = Intent(this, PayNowActivity::class.java)
                intent.putExtra("TOTAL_AMOUNT", totalSelectedAmount)
                intent.putExtra("ITEM_1_SELECTED", binding.cbItem1.isChecked)
                intent.putExtra("ITEM_2_SELECTED", binding.cbItem2.isChecked)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please select at least one sanction to pay.", Toast.LENGTH_SHORT).show()
            }
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

    private fun updateTotalDisplay() {
        binding.tvTotalAmount.text = "₱${String.format("%.2f", totalSelectedAmount)}"
    }
}
