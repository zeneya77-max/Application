package com.example.application

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.application.databinding.ActivityGcashOtpBinding

class GcashOtpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGcashOtpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGcashOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val totalAmount = intent.getDoubleExtra("TOTAL_AMOUNT", 0.0)
        val paymentMethod = intent.getStringExtra("PAYMENT_METHOD") ?: "GCash"

        setupOtpInputs()

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnSubmit.setOnClickListener {
            val otp = getOtpValue()
            if (otp.length == 6) {
                val intent = Intent(this, ConfirmPaymentActivity::class.java)
                intent.putExtra("TOTAL_AMOUNT", totalAmount)
                intent.putExtra("PAYMENT_METHOD", paymentMethod)
                startActivity(intent)
                finish()
            } else {
                // Show error or toast
            }
        }
    }

    private fun setupOtpInputs() {
        val otpFields = arrayOf(binding.otp1, binding.otp2, binding.otp3, binding.otp4, binding.otp5, binding.otp6)

        for (i in otpFields.indices) {
            otpFields[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s?.length == 1 && i < otpFields.size - 1) {
                        otpFields[i + 1].requestFocus()
                    }
                }
                override fun afterTextChanged(s: Editable?) {}
            })

            otpFields[i].setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
                    if (otpFields[i].text.isEmpty() && i > 0) {
                        otpFields[i - 1].requestFocus()
                    }
                }
                false
            }
        }
    }

    private fun getOtpValue(): String {
        return binding.otp1.text.toString() +
                binding.otp2.text.toString() +
                binding.otp3.text.toString() +
                binding.otp4.text.toString() +
                binding.otp5.text.toString() +
                binding.otp6.text.toString()
    }
}
