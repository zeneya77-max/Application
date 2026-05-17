package com.example.application

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomepageActivity : AppCompatActivity() {

    private val PREFS_NAME = "SancSeePrefs"
    private lateinit var layoutRecentActivity: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.student_homepage)

        layoutRecentActivity = findViewById(R.id.layoutRecentActivity)

        val sharedPref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        findViewById<TextView>(R.id.tvUserName).text = sharedPref.getString("saved_full_name", "Student")

        setupNavigation()
        loadRecentActivity()
        updateDashboardStats()
    }

    private fun setupNavigation() {
        val nav = findViewById<BottomNavigationView>(R.id.btnNavigation)
        nav.selectedItemId = R.id.nav_home
        nav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true
                R.id.nav_sanctions -> {
                    startActivity(Intent(this, MySanctionsActivity::class.java))
                    finish()
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.nav_notifications -> {
                    startActivity(Intent(this, StudentNotificationsActivity::class.java))
                    finish()
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    finish()
                    overridePendingTransition(0, 0)
                    true
                }
                else -> true
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadRecentActivity()
        updateDashboardStats()
    }

    private fun loadRecentActivity() {
        layoutRecentActivity.removeAllViews()
        val sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val activitySet = sharedPref.getStringSet("recent_activity_list", emptySet()) ?: emptySet()

        if (activitySet.isEmpty()) {
            val emptyText = TextView(this)
            emptyText.text = "No recent activities"
            emptyText.setTextColor(Color.GRAY)
            emptyText.setPadding(0, 20, 0, 0)
            layoutRecentActivity.addView(emptyText)
            return
        }

        // Sort by reversed order to show latest first
        val activityList = activitySet.toList().reversed()

        val inflater = LayoutInflater.from(this)
        for (activityData in activityList) {
            val parts = activityData.split("|")
            if (parts.size >= 4) {
                val name = parts[0]
                val timestamp = parts[1]
                val type = parts[2]
                val status = parts[3]

                val itemView = inflater.inflate(R.layout.item_recent_activity, null)
                val ivIcon = itemView.findViewById<ImageView>(R.id.ivIcon)
                val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
                val tvTimestamp = itemView.findViewById<TextView>(R.id.tvTimestamp)
                val tvStatus = itemView.findViewById<TextView>(R.id.tvStatus)

                tvTitle.text = name
                tvTimestamp.text = timestamp
                tvStatus.text = status

                if (type == "event") {
                    ivIcon.setImageResource(R.drawable.events)
                    tvStatus.setTextColor(Color.parseColor("#0288D1"))
                    tvStatus.setBackgroundResource(R.drawable.bg_status_upcoming)
                }

                layoutRecentActivity.addView(itemView)
            }
        }
    }

    private fun updateDashboardStats() {
        val sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val upcomingCount = sharedPref.getInt("upcoming_events_count", 0)
        findViewById<TextView>(R.id.tvUpcomingEventsCount).text = "$upcomingCount upcoming"
    }
}
