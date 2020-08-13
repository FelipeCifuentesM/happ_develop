package com.jumpitt.happ.ui.notifications

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jumpitt.happ.R
import com.jumpitt.happ.network.response.Notification
import kotlinx.android.synthetic.main.item_noti_list_header.view.*

class AdapterNotifications(
    private val context: Context,
    val notificationHistory: List<Notification>
) : RecyclerView.Adapter<AdapterNotifications.ViewHolder>() {

    private val viewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_noti_list_header, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.tag = position
        holder.tvAnimalType?.text = notificationHistory[position].dateVerbose

        val childLayoutManager = LinearLayoutManager(holder.itemView.rvNotiHistoryDetail.context, LinearLayoutManager.VERTICAL, false)

        holder.itemView.rvNotiHistoryDetail.apply {
            notificationHistory[position].data?.let { notificationDetail ->
                layoutManager = childLayoutManager
                adapter = AdapterNotificationsDetail(notificationDetail)
                setRecycledViewPool(viewPool)
            }
        }
    }

    override fun getItemCount(): Int {
        return notificationHistory.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvAnimalType = view.tvHeaderName
    }
}