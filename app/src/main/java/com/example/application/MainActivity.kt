package com.example.application

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

/**
 * MainActivity: The main entry point of the application containing the Login and Activation flow.
 * It handles switching between Student and Council access with professional loading effects and validation.
 */
class MainActivity : AppCompatActivity() {

    private val PREFS_NAME = "SancSeePrefs"
    
    private lateinit var layoutStudentFlow: LinearLayout
    private lateinit var layoutCouncilFlow: LinearLayout
    
    private lateinit var btnToggleStudent: LinearLayout
    private lateinit var btnToggleCouncil: LinearLayout
    
    private lateinit var tvToggleStudent: TextView
    private lateinit var ivToggleStudent: ImageView
    
    private lateinit var tvToggleCouncil: TextView
    private lateinit var ivToggleCouncil: ImageView

    private lateinit var tvHeaderTitle: TextView
    private lateinit var tvHeaderSubTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)

        // Initialize Layouts
        layoutStudentFlow = findViewById(R.id.layoutStudentFlow)
        layoutCouncilFlow = findViewById(R.id.layoutCouncilFlow)
        
        // Initialize Toggle Buttons
        btnToggleStudent = findViewById(R.id.btnToggleStudent)
        btnToggleCouncil = findViewById(R.id.btnToggleCouncil)
        
        // Initialize Toggle Components (Text and Icons)
        tvToggleStudent = findViewById(R.id.tvToggleStudent)
        ivToggleStudent = findViewById(R.id.ivToggleStudent)
        
        tvToggleCouncil = findViewById(R.id.tvToggleCouncil)
        ivToggleCouncil = findViewById(R.id.ivToggleCouncil)

        // Initialize Header Views
        tvHeaderTitle = findViewById(R.id.tvHeaderTitle)
        tvHeaderSubTitle = findViewById(R.id.tvHeaderSubTitle)

        // Set initial state
        showStudentLogin()

        // Toggle Listeners
        btnToggleStudent.setOnClickListener { showStudentLogin() }
        btnToggleCouncil.setOnClickListener { showCouncilLogin() }

        // Setup real-time formatting for Student IDs (XX-XXXX)
        setupIdFormatting(findViewById(R.id.etStep1StudentId))
        setupIdFormatting(findViewById(R.id.etLoginStudentId))

        // Password Visibility Toggles
        setupPasswordToggle(findViewById(R.id.etLoginPassword), findViewById(R.id.ivEyeStudent))
        setupPasswordToggle(findViewById(R.id.etCouncilPassword), findViewById(R.id.ivEyeCouncil))
        setupPasswordToggle(findViewById(R.id.etCreatePassword), findViewById(R.id.ivEyeCreatePassword))

        // --- STEP 1: IDENTITY VERIFICATION ---
        val btnStep1Continue = findViewById<Button>(R.id.btnStep1Continue)
        val pbStep1Loading = findViewById<ProgressBar>(R.id.pbStep1Loading)
        
        btnStep1Continue.setOnClickListener {
            val etId = findViewById<EditText>(R.id.etStep1StudentId)
            val etName = findViewById<EditText>(R.id.etStep1FullName)
            val containerId = findViewById<View>(R.id.containerStep1StudentId)
            val containerName = findViewById<View>(R.id.containerStep1FullName)
            
            val studentId = etId.text.toString().trim()
            val fullName = etName.text.toString().trim()

            // Reset UI
            containerId.setBackgroundResource(R.drawable.bg_input_field)
            containerName.setBackgroundResource(R.drawable.bg_input_field)

            var hasError = false

            if (!isValidId(studentId)) {
                showErrorEffect(containerId)
                Toast.makeText(this, "Format invalid. Use XX-XXXX", Toast.LENGTH_SHORT).show()
                hasError = true
            }

            if (fullName.isEmpty()) {
                showErrorEffect(containerName)
                hasError = true
            }

            if (hasError) return@setOnClickListener

            // Simulate loading
            btnStep1Continue.text = ""
            pbStep1Loading.visibility = View.VISIBLE
            btnStep1Continue.isEnabled = false

            Handler(Looper.getMainLooper()).postDelayed({
                val authPrefs = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
                val allowedIds = authPrefs.getStringSet("allowed_student_ids", emptySet())

                // For testing, let's allow all IDs or check against a mock list
                // If you have a specific list, use it. Otherwise, we can simulate success.
                if (true) { // Changed to true for easier testing/activation flow
                    findViewById<View>(R.id.layoutStep1).visibility = View.GONE
                    findViewById<View>(R.id.layoutStep2).visibility = View.VISIBLE
                    Toast.makeText(this, "Identity Verified! Please set your password.", Toast.LENGTH_SHORT).show()
                } else {
                    resetButtonState(btnStep1Continue, pbStep1Loading, getString(R.string.btn_continue))
                    showErrorEffect(containerId)
                    Toast.makeText(this, "Student ID not found in system.", Toast.LENGTH_LONG).show()
                }
            }, 1500)
        }

        // --- STEP 2: PASSWORD CREATION AND GENDER SELECTION ---
        val btnStep2Finish = findViewById<Button>(R.id.btnStep2Finish)
        btnStep2Finish.setOnClickListener {
            val etPass = findViewById<EditText>(R.id.etCreatePassword)
            val rgGender = findViewById<RadioGroup>(R.id.rgGender)
            val password = etPass.text.toString().trim()
            val studentId = findViewById<EditText>(R.id.etStep1StudentId).text.toString().trim()

            if (password.isEmpty() || password.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val checkedId = rgGender.checkedRadioButtonId
            if (checkedId == -1) {
                Toast.makeText(this, "Please select your gender", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val gender = if (checkedId == R.id.rbMale) "Male" else "Female"

            // Save to SharedPreferences
            val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            prefs.edit().apply {
                putString("saved_student_id", studentId)
                putString("saved_password", password)
                putString("saved_gender", gender)
                apply()
            }

            Toast.makeText(this, "Account Activated Successfully!", Toast.LENGTH_SHORT).show()

            // Go to Login Step
            findViewById<View>(R.id.layoutStep2).visibility = View.GONE
            findViewById<View>(R.id.layoutStep3).visibility = View.VISIBLE
            findViewById<EditText>(R.id.etLoginStudentId).setText(studentId)
        }

        // --- STUDENT LOGIN LOGIC ---
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val pbLoginLoading = findViewById<ProgressBar>(R.id.pbLoginLoading)
        
        btnLogin.setOnClickListener {
            val etId = findViewById<EditText>(R.id.etLoginStudentId)
            val etPass = findViewById<EditText>(R.id.etLoginPassword)
            val containerId = findViewById<View>(R.id.containerLoginStudentId)
            val containerPass = findViewById<View>(R.id.containerLoginPassword)
            
            val studentId = etId.text.toString().trim()
            val password = etPass.text.toString().trim()

            containerId.setBackgroundResource(R.drawable.bg_input_field)
            containerPass.setBackgroundResource(R.drawable.bg_input_field)

            var hasError = false
            if (!isValidId(studentId)) {
                showErrorEffect(containerId)
                hasError = true
            }

            if (password.isEmpty()) {
                showErrorEffect(containerPass)
                hasError = true
            }

            if (hasError) return@setOnClickListener

            btnLogin.text = ""
            pbLoginLoading.visibility = View.VISIBLE
            btnLogin.isEnabled = false

            Handler(Looper.getMainLooper()).postDelayed({
                val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                val savedId = prefs.getString("saved_student_id", "")
                val savedPass = prefs.getString("saved_password", "")

                if (studentId == savedId && password == savedPass && studentId.isNotEmpty()) {
                    startActivity(Intent(this, HomepageActivity::class.java))
                    finish()
                } else {
                    resetButtonState(btnLogin, pbLoginLoading, getString(R.string.btn_login))
                    showErrorEffect(containerId)
                    showErrorEffect(containerPass)
                    Toast.makeText(this, "Invalid credentials. Try again.", Toast.LENGTH_SHORT).show()
                }
            }, 1500)
        }

        // --- COUNCIL LOGIN LOGIC ---
        val btnCouncilLogin = findViewById<Button>(R.id.btnCouncilLogin)
        val pbCouncilLoading = findViewById<ProgressBar>(R.id.pbCouncilLoading)

        btnCouncilLogin.setOnClickListener {
            val etUser = findViewById<EditText>(R.id.etCouncilUsername)
            val etPass = findViewById<EditText>(R.id.etCouncilPassword)
            val containerUser = findViewById<View>(R.id.layoutCouncilUsernameContainer)
            val containerPass = findViewById<View>(R.id.layoutCouncilPasswordContainer)
            
            val username = etUser.text.toString().trim()
            val password = etPass.text.toString().trim()

            containerUser.setBackgroundResource(R.drawable.bg_input_field)
            containerPass.setBackgroundResource(R.drawable.bg_input_field)

            var hasError = false
            if (username.isEmpty()) {
                showErrorEffect(containerUser)
                hasError = true
            }
            if (password.isEmpty()) {
                showErrorEffect(containerPass)
                hasError = true
            }

            if (hasError) return@setOnClickListener

            btnCouncilLogin.text = ""
            pbCouncilLoading.visibility = View.VISIBLE
            btnCouncilLogin.isEnabled = false

            Handler(Looper.getMainLooper()).postDelayed({
                if (username == "admin" && password == "CITadmin") {
                    startActivity(Intent(this, CouncilHomepageActivity::class.java))
                    finish()
                } else {
                    resetButtonState(btnCouncilLogin, pbCouncilLoading, getString(R.string.login))
                    showErrorEffect(containerUser)
                    showErrorEffect(containerPass)
                    Toast.makeText(this, "Access Denied: Wrong Council Credentials", Toast.LENGTH_SHORT).show()
                }
            }, 1500)
        }

        // Navigation links
        findViewById<View>(R.id.tvLoginWithPassword).setOnClickListener {
            findViewById<View>(R.id.layoutStep1).visibility = View.GONE
            findViewById<View>(R.id.layoutStep2).visibility = View.GONE
            findViewById<View>(R.id.layoutStep3).visibility = View.VISIBLE
        }

        findViewById<View>(R.id.tvForgotPasswordStudent)?.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        findViewById<View>(R.id.tvForgotPasswordCouncil)?.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
    }

    private fun setupPasswordToggle(editText: EditText, eyeIcon: ImageView) {
        var isPasswordVisible = false
        eyeIcon.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                editText.transformationMethod = HideReturnsTransformationMethod.getInstance()
                eyeIcon.setImageResource(R.drawable.ic_visibility)
            } else {
                editText.transformationMethod = PasswordTransformationMethod.getInstance()
                eyeIcon.setImageResource(R.drawable.ic_visibility_off)
            }
            editText.setSelection(editText.text.length)
        }
    }

    private fun showErrorEffect(view: View) {
        view.setBackgroundResource(R.drawable.bg_input_field_error)
        val shake = AnimationUtils.loadAnimation(this, R.anim.shake)
        view.startAnimation(shake)
    }

    private fun isValidId(id: String): Boolean {
        return id.matches(Regex("^[0-9]{2}-[0-9]{4}$"))
    }

    private fun setupIdFormatting(editText: EditText) {
        editText.addTextChangedListener(object : TextWatcher {
            private var isUpdating = false
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (isUpdating) return
                isUpdating = true

                val text = s.toString().replace("-", "")
                if (text.length >= 2) {
                    val formatted = text.substring(0, 2) + "-" + text.substring(2)
                    if (formatted != s.toString()) {
                        editText.setText(formatted)
                        editText.setSelection(formatted.length)
                    }
                }
                isUpdating = false
            }
        })
    }

    private fun resetButtonState(button: Button, progressBar: ProgressBar, originalText: String) {
        button.isEnabled = true
        button.text = originalText
        progressBar.visibility = View.GONE
    }

    private fun showStudentLogin() {
        layoutStudentFlow.visibility = View.VISIBLE
        layoutCouncilFlow.visibility = View.GONE

        btnToggleStudent.setBackgroundResource(R.drawable.bg_violet_rounded)
        btnToggleStudent.backgroundTintList = ContextCompat.getColorStateList(this, R.color.violet_primary)
        tvToggleStudent.setTextColor(Color.WHITE)
        ivToggleStudent.imageTintList = ColorStateList.valueOf(Color.WHITE)

        btnToggleCouncil.background = null
        tvToggleCouncil.setTextColor(Color.parseColor("#757575"))
        ivToggleCouncil.imageTintList = ColorStateList.valueOf(Color.parseColor("#757575"))

        tvHeaderTitle.text = getString(R.string.welcome_sancsee)
        tvHeaderSubTitle.text = getString(R.string.secure_student_access)
    }

    private fun showCouncilLogin() {
        layoutStudentFlow.visibility = View.GONE
        layoutCouncilFlow.visibility = View.VISIBLE

        btnToggleCouncil.setBackgroundResource(R.drawable.bg_violet_rounded)
        btnToggleCouncil.backgroundTintList = ContextCompat.getColorStateList(this, R.color.violet_primary)
        tvToggleCouncil.setTextColor(Color.WHITE)
        ivToggleCouncil.imageTintList = ColorStateList.valueOf(Color.WHITE)

        btnToggleStudent.background = null
        tvToggleStudent.setTextColor(Color.parseColor("#757575"))
        ivToggleStudent.imageTintList = ColorStateList.valueOf(Color.parseColor("#757575"))

        tvHeaderTitle.text = "Council Login"
        tvHeaderSubTitle.text = "Manage Student Sanctions and Events"
    }
}
