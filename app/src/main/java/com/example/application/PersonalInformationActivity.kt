package com.example.application

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class PersonalInformationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_information)
        findViewById<View>(R.id.btnBack).setOnClickListener { finish() }
    }
}
