package com.example.application

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.application.databinding.ConfirmPaymentScreenBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ConfirmPaymentActivity : AppCompatActivity() {
    private lateinit var binding: ConfirmPaymentScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ConfirmPaymentScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val amount = intent.getDoubleExtra("TOTAL_AMOUNT", 0.0)
        val method = intent.getStringExtra("PAYMENT_METHOD") ?: "GCash"

        binding.tvPaymentTitle.text = method
        binding.tvConfirmAmount.text = String.format(Locale.getDefault(), "₱ %.2f", amount)
        
        // Mocking merchant and reference number as seen in the screenshot
        binding.tvMerchantName.text = "Sancee"
        binding.tvReferenceNo.text = "5306 478 8924"
        
        val sdf = SimpleDateFormat("MMM dd, yyyy, hh:mm a", Locale.getDefault())
        val currentDateAndTime = sdf.format(Date())
        binding.tvDateTime.text = currentDateAndTime

        binding.btnDone.text = "Back to Sancee"
        binding.btnDone.setOnClickListener {
            val intent = Intent(this, MySanctionsActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
}
