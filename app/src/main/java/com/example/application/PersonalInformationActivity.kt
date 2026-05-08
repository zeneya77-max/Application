package com.example.application

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.application.databinding.ActivityPersonalInformationBinding

class PersonalInformationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPersonalInformationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        setupProfileInfo()

        binding.btnSaveChanges.setOnClickListener {
            finish()
        }
    }

    private fun setupProfileInfo() {
        // Personal Information Section
        binding.layoutFullName.apply {
            ivIcon.setImageResource(R.drawable.ic_person)
            tvLabel.text = getString(R.string.full_name)
            tvValue.text = getString(R.string.profile_name_sample)
        }

        binding.layoutDob.apply {
            ivIcon.setImageResource(R.drawable.ic_info) // Using ic_info as a placeholder for calendar
            tvLabel.text = getString(R.string.date_of_birth)
            tvValue.text = getString(R.string.sample_dob)
        }

        binding.layoutEmail.apply {
            ivIcon.setImageResource(R.drawable.ic_notifications) // Using ic_notifications as placeholder for email
            tvLabel.text = getString(R.string.email_address)
            tvValue.text = getString(R.string.profile_email_sample)
        }

        binding.layoutPhone.apply {
            ivIcon.setImageResource(R.drawable.ic_person) // Placeholder
            tvLabel.text = getString(R.string.phone_number)
            tvValue.text = getString(R.string.profile_phone_sample)
        }

        binding.layoutStudentId.apply {
            ivIcon.setImageResource(R.drawable.ic_student_cap)
            tvLabel.text = getString(R.string.student_employee_id)
            tvValue.text = getString(R.string.sample_student_id)
        }
    }
}
