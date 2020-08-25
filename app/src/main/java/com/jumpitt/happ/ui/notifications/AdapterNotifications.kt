package com.jumpitt.happ.ui.notifications

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.jumpitt.happ.R
import com.jumpitt.happ.network.response.Notification
import com.jumpitt.happ.utils.ColorIdResource
import com.jumpitt.happ.utils.ItemRecyclerview
import com.jumpitt.happ.utils.Labelstext
import com.jumpitt.happ.utils.containedStyle
import kotlinx.android.synthetic.main.item_noti_history_empty.view.*
import kotlinx.android.synthetic.main.item_noti_list_header.view.*
import kotlinx.android.synthetic.main.item_skeleton_noti_history.view.*

class AdapterNotifications(
    private val context: Context,
    private val notificationHistory: List<Notification>,
    private val shimmerNotificationHistory: ShimmerFrameLayout
) : RecyclerView.Adapter<AdapterNotifications.ViewHolder>() {

    private val viewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return if(notificationHistory.isNullOrEmpty()){
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_noti_history_empty, parent, false))
        }else{
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_noti_list_header, parent, false))
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(notificationHistory.isNullOrEmpty()){
            holder.bindItemsEmpty(shimmerNotificationHistory)
        }else{
            holder.bindItems(notificationHistory[position], viewPool)
        }

    }

    override fun getItemCount(): Int {
        return if(notificationHistory.isNullOrEmpty())
            ItemRecyclerview.EMPTY
        else
            notificationHistory.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvNotiDateHeader = view.tvNotiDateHeader
        private val tvEmptyNotiHistory = view.tvEmptyNotiHistory

        fun bindItemsEmpty(shimmerNotificationHistory: ShimmerFrameLayout){
            tvEmptyNotiHistory.containedStyle(Labelstext.BODY1, ColorIdResource.LIGHTDISABLED, font =  R.font.dmsans_medium)

            shimmerNotificationHistory.stopShimmer()
            shimmerNotificationHistory.visibility = View.GONE
        }

        fun bindItems(itemNotification: Notification, viewPool: RecyclerView.RecycledViewPool){
            tvNotiDateHeader.containedStyle(Labelstext.BODY1, ColorIdResource.LIGHTDISABLED, font =  R.font.dmsans_medium)

            itemNotification.dateVerbose?.let { dateHeader -> tvNotiDateHeader?.text = dateHeader.capitalize() }

            val childLayoutManager = LinearLayoutManager(itemView.rvNotiHistoryDetail.context, LinearLayoutManager.VERTICAL, false)
            itemView.rvNotiHistoryDetail.apply {
                itemNotification.data?.let { notificationDetail ->
                    layoutManager = childLayoutManager
                    adapter = AdapterNotificationsDetail(notificationDetail)
                    setRecycledViewPool(viewPool)
                }
            }
        }
    }
}