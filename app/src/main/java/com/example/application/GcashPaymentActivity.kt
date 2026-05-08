package com.example.application

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.application.databinding.ActivityGcashPaymentBinding
import java.util.Locale

class GcashPaymentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGcashPaymentBinding
    private var isDetailsVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGcashPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val totalAmount = intent.getDoubleExtra("TOTAL_AMOUNT", 0.0)
        binding.tvAmountToPay.text = String.format(Locale.getDefault(), "₱ %.2f", totalAmount)

        // Setup Selected Sanctions Details
        val isItem1Selected = intent.getBooleanExtra("ITEM_1_SELECTED", false)
        val isItem2Selected = intent.getBooleanExtra("ITEM_2_SELECTED", false)

        binding.tvDetailItem1.visibility = if (isItem1Selected) View.VISIBLE else View.GONE
        binding.tvDetailItem2.visibility = if (isItem2Selected) View.VISIBLE else View.GONE

        // Toggle Details Click Listener
        binding.ivToggleDetails.setOnClickListener {
            toggleDetails()
        }

        // Also allow clicking the banner to toggle
        binding.llAlertBanner.setOnClickListener {
            toggleDetails()
        }

        binding.btnNext.setOnClickListener {
            val mobileNumber = binding.etMobileNumber.text.toString()
            if (mobileNumber.isNotEmpty()) {
                val intent = Intent(this, GcashOtpActivity::class.java)
                intent.putExtra("TOTAL_AMOUNT", totalAmount)
                intent.putExtra("PAYMENT_METHOD", "GCash")
                startActivity(intent)
            } else {
                binding.etMobileNumber.error = "Please enter mobile number"
            }
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun toggleDetails() {
        isDetailsVisible = !isDetailsVisible
        binding.llPaymentDetails.visibility = if (isDetailsVisible) View.VISIBLE else View.GONE
        // Rotate arrow icon: 180 degrees if visible, 0 if hidden
        binding.ivToggleDetails.rotation = if (isDetailsVisible) 180f else 0f
    }
}
