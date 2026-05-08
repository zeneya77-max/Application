package com.example.application

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.application.databinding.ItemRecentActivityBinding

class RecentActivityAdapter(private val activities: List<ActivityRecord>) :
    RecyclerView.Adapter<RecentActivityAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemRecentActivityBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecentActivityBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val activity = activities[position]
        with(holder.binding) {
            tvTitle.text = activity.title
            tvTimestamp.text = activity.timestamp
            
            if (activity.amount != null) {
                tvAmount.visibility = View.VISIBLE
                tvAmount.text = activity.amount
            } else {
                tvAmount.visibility = View.GONE
            }

            tvStatus.text = activity.status
            tvStatus.setTextColor(ContextCompat.getColor(root.context, activity.statusColorResId))
            tvStatus.setBackgroundResource(activity.statusBgResId)

            ivIcon.setImageResource(activity.iconResId)
            ivIcon.setColorFilter(ContextCompat.getColor(root.context, activity.iconTintResId))
            iconCard.setCardBackgroundColor(ContextCompat.getColor(root.context, activity.iconBgResId))
        }
    }

    override fun getItemCount() = activities.size
}
