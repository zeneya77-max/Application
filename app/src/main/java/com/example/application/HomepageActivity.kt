package com.example.application

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.application.databinding.HomepageBinding
import java.io.File

class HomepageActivity : AppCompatActivity() {
    private lateinit var binding: HomepageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadProfilePicture()

        // View All Recent Activity
        binding.btnViewAll.setOnClickListener {
            val intent = Intent(this, RecentActivityActivity::class.java)
            startActivity(intent)
        }

        // Quick Actions
        binding.sanctionsAction.setOnClickListener {
            val intent = Intent(this, MySanctionsActivity::class.java)
            startActivity(intent)
        }

        binding.btnNavigation.selectedItemId = R.id.nav_home
        binding.btnNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true
                R.id.nav_sanctions -> {
                    startActivity(Intent(this, MySanctionsActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_notifications -> {
                    startActivity(Intent(this, NotificationsActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadProfilePicture()
    }

    private fun loadProfilePicture() {
        val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val path = sharedPref.getString("profile_picture_path", null)
        path?.let {
            val file = File(it)
            if (file.exists()) {
                binding.avatarCard.setPadding(0, 0, 0, 0)
                val imageView = binding.avatarCard.getChildAt(0) as? android.widget.ImageView
                imageView?.apply {
                    setImageURI(Uri.fromFile(file))
                    scaleType = android.widget.ImageView.ScaleType.CENTER_CROP
                }
            }
        }
    }
}
