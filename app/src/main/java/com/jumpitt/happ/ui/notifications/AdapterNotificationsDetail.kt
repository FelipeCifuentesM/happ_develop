package com.jumpitt.happ.ui.notifications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jumpitt.happ.R
import com.jumpitt.happ.network.response.Data
import kotlinx.android.synthetic.main.item_noti_list_detail.view.*

class AdapterNotificationsDetail(
    val subHeadingData: List<Data>

) : RecyclerView.Adapter<ViewHolder>() {

    override fun getItemCount(): Int {
        return subHeadingData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvDescription?.text = subHeadingData[position].body
        holder.tvTime?.text = subHeadingData[position].createdAt
        holder.ivNotiIcon.setBackgroundResource(R.drawable.ic_launcher_foreground)
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_noti_list_detail, parent, false))
    }
}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tvDescription = view.tvNotificationDescription
    val tvTime = view.tvTimeNotification
    val ivNotiIcon = view.ivNotiHistoryIcon
}
