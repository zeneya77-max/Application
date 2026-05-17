package com.example.application

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class RecentActivityActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recent_activity_screen)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar?.setNavigationOnClickListener {
            finish()
        }
    }
}
