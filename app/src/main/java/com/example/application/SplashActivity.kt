package com.example.application

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val splashBg = findViewById<View>(R.id.ivSplashBg)
        val glassCard = findViewById<View>(R.id.glassCard)
        val ivLogo = findViewById<View>(R.id.ivLogo)
        val tvAppName = findViewById<View>(R.id.tvAppName)
        val tvSubtitle = findViewById<View>(R.id.tvSubtitle)
        val vLogoGlow = findViewById<View>(R.id.vLogoGlow)
        val ivRingOuter = findViewById<View>(R.id.ivRingOuter)
        val ivRingInner = findViewById<View>(R.id.ivRingInner)
        val vLightSweep = findViewById<View>(R.id.vLightSweep)
        val p1 = findViewById<View>(R.id.p1)
        val p2 = findViewById<View>(R.id.p2)
        val p3 = findViewById<View>(R.id.p3)

        // 1. Background Parallax Motion
        ObjectAnimator.ofFloat(splashBg, View.TRANSLATION_X, -50f, 50f).apply {
            duration = 10000
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            interpolator = LinearInterpolator()
            start()
        }

        // 2. Circular Ring Animation
        ivRingOuter.alpha = 0f
        ivRingInner.alpha = 0f
        ivRingOuter.animate().alpha(0.2f).setDuration(1500).start()
        ivRingInner.animate().alpha(0.4f).setDuration(1500).start()

        ObjectAnimator.ofFloat(ivRingOuter, View.ROTATION, 0f, 360f).apply {
            duration = 8000
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            start()
        }
        ObjectAnimator.ofFloat(ivRingInner, View.ROTATION, 0f, -360f).apply {
            duration = 6000
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            start()
        }

        // 3. Glassmorphism Reveal + Fade & Slide Entrance
        glassCard.translationY = 100f
        glassCard.alpha = 0f
        glassCard.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(1500)
            .setInterpolator(DecelerateInterpolator())
            .setStartDelay(300)
            .start()

        // 4. Logo Glow Pulse
        val pulseAnim = AnimatorSet().apply {
            val scaleX = ObjectAnimator.ofFloat(vLogoGlow, View.SCALE_X, 1f, 1.4f)
            val scaleY = ObjectAnimator.ofFloat(vLogoGlow, View.SCALE_Y, 1f, 1.4f)
            val alpha = ObjectAnimator.ofFloat(vLogoGlow, View.ALPHA, 0f, 0.3f, 0f)
            
            playTogether(scaleX, scaleY, alpha)
            duration = 3000
            interpolator = AccelerateDecelerateInterpolator()
        }
        
        pulseAnim.addListener(object : android.animation.AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                pulseAnim.start()
            }
        })
        pulseAnim.start()

        // 5. Particle Motion Animation
        fun startParticleAnim(view: View, startX: Float, startY: Float, duration: Long) {
            view.translationX = startX
            view.translationY = startY
            
            ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, startY, startY - 200f).apply {
                this.duration = duration
                repeatCount = ValueAnimator.INFINITE
                repeatMode = ValueAnimator.RESTART
                interpolator = LinearInterpolator()
                start()
            }
            ObjectAnimator.ofFloat(view, View.ALPHA, 0f, 0.6f, 0f).apply {
                this.duration = duration
                repeatCount = ValueAnimator.INFINITE
                repeatMode = ValueAnimator.RESTART
                interpolator = LinearInterpolator()
                start()
            }
        }

        startParticleAnim(p1, 200f, 1500f, 5000)
        startParticleAnim(p2, 600f, 1300f, 7000)
        startParticleAnim(p3, 800f, 1700f, 6000)

        // 6. Final Transition
        Handler(Looper.getMainLooper()).postDelayed({
            vLightSweep.visibility = View.VISIBLE
            vLightSweep.animate()
                .alpha(1f)
                .setDuration(800)
                .setListener(object : android.animation.AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        val intent = Intent(this@SplashActivity, MainActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(0, 0)
                        finish()
                    }
                }).start()
        }, 4500)
    }
}
