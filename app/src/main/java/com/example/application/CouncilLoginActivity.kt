package com.example.application

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CouncilLoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.council_login)

        val etUsername = findViewById<EditText>(R.id.etCouncilUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val containerUser = findViewById<View>(R.id.layoutCouncilUsernameContainer)
        val containerPass = findViewById<View>(R.id.layoutPasswordContainer)
        val btnLogin = findViewById<Button>(R.id.loginButton)
        val tvForgotPassword = findViewById<View>(R.id.tvForgotPassword)
        val ivEyeToggle = findViewById<ImageView>(R.id.eyeToggle)

        // Password Visibility Toggle
        var isPasswordVisible = false
        ivEyeToggle.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                ivEyeToggle.setImageResource(R.drawable.ic_visibility)
            } else {
                etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                ivEyeToggle.setImageResource(R.drawable.ic_visibility_off)
            }
            etPassword.setSelection(etPassword.text.length)
        }

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            // Reset backgrounds
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

            if (username == "admin" && password == "CITadmin") {
                startActivity(Intent(this, CouncilHomepageActivity::class.java))
                finish()
            } else {
                showErrorEffect(containerUser)
                showErrorEffect(containerPass)
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
            }
        }

        tvForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        findViewById<View>(R.id.btnStudentToggle).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun showErrorEffect(view: View) {
        view.setBackgroundResource(R.drawable.bg_input_field_error)
        val shake = AnimationUtils.loadAnimation(this, R.anim.shake)
        view.startAnimation(shake)
    }
}
