package com.example.application

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.application.databinding.ProfileScreenBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ProfileScreenBinding
    private var photoUri: Uri? = null

    // Launcher for Camera Permission
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            openCamera()
        } else {
            Toast.makeText(this, "Camera permission is required to take photos", Toast.LENGTH_SHORT).show()
        }
    }

    // Launcher for Gallery Selection
    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            saveAndDisplayProfilePicture(it)
        }
    }

    // Launcher for Camera Result
    private val takePhotoLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            photoUri?.let { uri ->
                saveAndDisplayProfilePicture(uri)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ProfileScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load existing profile picture or gender-based default
        loadProfilePicture()

        // Set listeners for changing profile picture
        binding.ivProfilePicture.setOnClickListener {
            showImagePickerOptions()
        }

        // Navigation to Personal Information
        binding.btnPersonalInfo.setOnClickListener {
            startActivity(Intent(this, PersonalInformationActivity::class.java))
        }

        // Setup Bottom Navigation
        binding.btnNavigation.selectedItemId = R.id.nav_profile
        binding.btnNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, HomepageActivity::class.java))
                    finish()
                    true
                }
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
                R.id.nav_profile -> true
                else -> false
            }
        }

        binding.btnLogout.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun showImagePickerOptions() {
        val options = arrayOf("Take Photo", "Choose from Gallery", "Cancel")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Update Profile Picture")
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> checkCameraPermission()
                1 -> openGallery()
                2 -> dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            openCamera()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun openCamera() {
        val photoFile: File? = try {
            createImageFile()
        } catch (ex: IOException) {
            Toast.makeText(this, "Error creating file", Toast.LENGTH_SHORT).show()
            null
        }

        photoFile?.let { file ->
            photoUri = FileProvider.getUriForFile(
                this,
                "${packageName}.provider",
                file
            )
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            takePhotoLauncher.launch(intent)
        }
    }

    private fun openGallery() {
        pickImageLauncher.launch("image/*")
    }

    private fun saveAndDisplayProfilePicture(uri: Uri) {
        try {
            val inputStream = contentResolver.openInputStream(uri)
            val profileFile = File(filesDir, "profile_picture.jpg")
            val outputStream = profileFile.outputStream()
            
            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }

            val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString("profile_picture_path", profileFile.absolutePath)
                apply()
            }

            displayImage(profileFile)
            Toast.makeText(this, "Profile picture updated", Toast.LENGTH_SHORT).show()
            
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to save profile picture", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadProfilePicture() {
        val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val path = sharedPref.getString("profile_picture_path", null)
        
        if (path != null) {
            val file = File(path)
            if (file.exists()) {
                displayImage(file)
                return
            }
        }
        
        // If no custom photo, show default based on gender
        val gender = sharedPref.getString("user_gender", "male")
        if (gender == "female") {
            binding.ivProfilePicture.setImageResource(R.drawable.female)
            binding.ivProfilePicture.imageTintList = null
            binding.ivProfilePicture.setPadding(0, 0, 0, 0)
            binding.ivProfilePicture.scaleType = android.widget.ImageView.ScaleType.CENTER_CROP
        } else if (gender == "male") {
            binding.ivProfilePicture.setImageResource(R.drawable.male)
            binding.ivProfilePicture.imageTintList = null
            binding.ivProfilePicture.setPadding(0, 0, 0, 0)
            binding.ivProfilePicture.scaleType = android.widget.ImageView.ScaleType.CENTER_CROP
        } else {
            binding.ivProfilePicture.setImageResource(R.drawable.ic_person)
        }
    }

    private fun displayImage(file: File) {
        binding.ivProfilePicture.apply {
            setImageURI(null)
            setImageURI(Uri.fromFile(file))
            setPadding(0, 0, 0, 0)
            scaleType = android.widget.ImageView.ScaleType.CENTER_CROP
            imageTintList = null
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
    }
}
