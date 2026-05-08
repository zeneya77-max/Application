package com.example.application

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.application.databinding.RecentActivityScreenBinding

class RecentActivityActivity : AppCompatActivity() {
    private lateinit var binding: RecentActivityScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RecentActivityScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val activities = listOf(
            ActivityRecord(
                "Paid sanction — ₱36.00",
                "25 Apr 2026 • 10:15 AM",
                "₱36.00",
                "confirmed",
                R.drawable.sancsicon,
                R.color.violet_primary,
                R.color.upcoming_bg,
                R.color.confirmed_text,
                R.drawable.bg_status_confirmed
            ),
            ActivityRecord(
                "New sanction: Late attendance",
                "24 Apr 2026 • 03:42 PM",
                "₱50.00",
                "unpaid",
                R.drawable.sancsicon,
                R.color.incomplete_text,
                R.color.incomplete_bg,
                R.color.unpaid_text,
                R.drawable.bg_status_unpaid
            ),
            ActivityRecord(
                "Joined event: Clean-up Drive",
                "23 Apr 2026 • 01:20 PM",
                null,
                "pending",
                R.drawable.events,
                R.color.confirmed_text,
                R.color.confirmed_bg,
                R.color.pending_text,
                R.drawable.bg_status_pending
            ),
            ActivityRecord(
                "Upcoming event: Tree Planting",
                "22 Apr 2026 • 09:00 AM",
                null,
                "upcoming",
                R.drawable.events,
                R.color.pending_text,
                R.color.pending_bg,
                R.color.upcoming_text,
                R.drawable.bg_status_upcoming
            ),
            ActivityRecord(
                "Logged service — 3 hours",
                "21 Apr 2026 • 04:35 PM",
                null,
                "pending approval",
                R.drawable.service,
                R.color.unpaid_text,
                R.color.unpaid_bg,
                R.color.unpaid_text,
                R.drawable.bg_status_pending_approval
            ),
            ActivityRecord(
                "Service approved — 2 hours",
                "20 Apr 2026 • 11:10 AM",
                null,
                "confirmed",
                R.drawable.service,
                R.color.confirmed_text,
                R.color.confirmed_bg,
                R.color.confirmed_text,
                R.drawable.bg_status_confirmed
            ),
            ActivityRecord(
                "Missed event: Seminar",
                "19 Apr 2026 • 02:00 PM",
                null,
                "incomplete",
                R.drawable.events,
                R.color.incomplete_text,
                R.color.incomplete_bg,
                R.color.incomplete_text,
                R.drawable.bg_status_incomplete
            )
        )

        binding.rvRecentActivity.layoutManager = LinearLayoutManager(this)
        binding.rvRecentActivity.adapter = RecentActivityAdapter(activities)
    }
}
