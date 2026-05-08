package com.example.application

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.application.databinding.PayNowScreenBinding
import java.util.Locale

class PayNowActivity : AppCompatActivity() {
    private lateinit var binding: PayNowScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PayNowScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the total amount passed from the previous activity
        val totalAmount = intent.getDoubleExtra("TOTAL_AMOUNT", 0.0)
        binding.tvTotalAmountPay.text = String.format(Locale.getDefault(), "₱%.2f", totalAmount)

        // Show/Hide summary items based on selection from UnpaidSanctionsActivity
        val isItem1Selected = intent.getBooleanExtra("ITEM_1_SELECTED", false)
        val isItem2Selected = intent.getBooleanExtra("ITEM_2_SELECTED", false)

        binding.tvSummaryItem1.visibility = if (isItem1Selected) View.VISIBLE else View.GONE
        binding.tvSummaryItem2.visibility = if (isItem2Selected) View.VISIBLE else View.GONE

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnConfirmPayment.setOnClickListener {
            // Since GCash is the only option and it's checked by default
            if (binding.rbGCash.isChecked) {
                // Proceed to GcashPaymentActivity
                val intent = Intent(this, GcashPaymentActivity::class.java)
                intent.putExtra("TOTAL_AMOUNT", totalAmount)
                intent.putExtra("ITEM_1_SELECTED", isItem1Selected)
                intent.putExtra("ITEM_2_SELECTED", isItem2Selected)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
