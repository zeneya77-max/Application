package com.example.application

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class NotificationsActivity : AppCompatActivity() {

    private val PREFS_NAME = "SancSeePrefs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.notifications_screen)

        setupNavigation()
        loadNotifications()
    }

    private fun setupNavigation() {
        val nav = findViewById<BottomNavigationView>(R.id.btnNavigation)
        nav.selectedItemId = R.id.nav_notifications
        nav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, HomepageActivity::class.java))
                    finish()
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.nav_sanctions -> {
                    startActivity(Intent(this, MySanctionsActivity::class.java))
                    finish()
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.nav_notifications -> true
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

    private fun loadNotifications() {
        val layoutNotifList = findViewById<LinearLayout>(R.id.layoutNotificationsList)
        layoutNotifList.removeAllViews()

        val sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val notifSet = sharedPref.getStringSet("notifications_list", emptySet()) ?: emptySet()

        if (notifSet.isEmpty()) {
            val tvEmpty = TextView(this)
            tvEmpty.text = "No notifications yet"
            tvEmpty.gravity = android.view.Gravity.CENTER
            tvEmpty.setPadding(0, 50, 0, 0)
            layoutNotifList.addView(tvEmpty)
            return
        }

        val inflater = LayoutInflater.from(this)
        // Reverse to show newest first
        val notifList = notifSet.toList().sortedByDescending { it.split("|").getOrElse(2) { "" } }
        
        notifSet.toList().reversed().forEach { notifData ->
            val parts = notifData.split("|")
            if (parts.size >= 3) {
                val title = parts[0]
                val message = parts[1]
                val time = parts[2]

                val notifView = inflater.inflate(R.layout.item_notification_dynamic, layoutNotifList, false)
                notifView.findViewById<TextView>(R.id.tvNotifTitle).text = title
                notifView.findViewById<TextView>(R.id.tvNotifMessage).text = message
                notifView.findViewById<TextView>(R.id.tvNotifTime).text = time
                
                val btnDelete = notifView.findViewById<ImageView>(R.id.btnDeleteNotif)
                btnDelete.setOnClickListener {
                    showDeleteConfirmation(notifData)
                }
                
                layoutNotifList.addView(notifView)
            }
        }
    }

    private fun showDeleteConfirmation(notifData: String) {
        AlertDialog.Builder(this)
            .setTitle("Delete Notification")
            .setMessage("Are you sure you want to delete this notification?")
            .setPositiveButton("Delete") { _, _ ->
                deleteNotification(notifData)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deleteNotification(notifData: String) {
        val sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val notifSet = sharedPref.getStringSet("notifications_list", mutableSetOf())?.toMutableSet() ?: mutableSetOf()
        
        if (notifSet.remove(notifData)) {
            sharedPref.edit().putStringSet("notifications_list", notifSet).apply()
            loadNotifications() // Refresh the UI
            Toast.makeText(this, "Notification deleted", Toast.LENGTH_SHORT).show()
        }
    }
}
