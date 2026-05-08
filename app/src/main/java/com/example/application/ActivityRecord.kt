package com.example.application

data class ActivityRecord(
    val title: String,
    val timestamp: String,
    val amount: String? = null,
    val status: String,
    val iconResId: Int,
    val iconTintResId: Int,
    val iconBgResId: Int,
    val statusColorResId: Int,
    val statusBgResId: Int
)
