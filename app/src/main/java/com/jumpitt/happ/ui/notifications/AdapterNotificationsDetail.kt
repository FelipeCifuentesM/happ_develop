package com.jumpitt.happ.ui.notifications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jumpitt.happ.R
import com.jumpitt.happ.network.response.Data
import com.jumpitt.happ.utils.ColorIdResource
import com.jumpitt.happ.utils.Labelstext
import com.jumpitt.happ.utils.TypeNotification
import com.jumpitt.happ.utils.containedStyle
import kotlinx.android.synthetic.main.item_noti_list_detail.view.*

class AdapterNotificationsDetail(
    private val subHeadingData: List<Data>

) : RecyclerView.Adapter<ViewHolder>() {

    override fun getItemCount(): Int {
        return subHeadingData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvTime.containedStyle(Labelstext.BODY2, ColorIdResource.LIGHTDISABLED, font =  R.font.dmsans_medium)
        holder.tvNotiTitle.containedStyle(Labelstext.BODY1, ColorIdResource.BLACK, font =  R.font.dmsans_medium)
        holder.tvNotiDescription.containedStyle(Labelstext.BODY2, ColorIdResource.BLACK)

        subHeadingData[position].time?.let { time -> holder.tvTime?.text = time }
        subHeadingData[position].title?.let { title -> holder.tvNotiTitle?.text = title }
        subHeadingData[position].body?.let { description -> holder.tvNotiDescription?.text = description }

        subHeadingData[position].type?.let { type ->
            when(type){
                TypeNotification.SOCIAL_DISTANCE -> holder.ivNotiIcon.setBackgroundResource(R.drawable.ic_people_arrows_duotone)
                TypeNotification.HEALTHCARE_RISK -> holder.ivNotiIcon.setBackgroundResource(R.drawable.ic_vial_duotone)
                else -> holder.ivNotiIcon.setBackgroundResource(R.drawable.ic_happ_transparent)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_noti_list_detail, parent, false))
    }
}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tvTime = view.tvTimeNotification
    val tvNotiTitle = view.tvNotificationTitle
    val tvNotiDescription = view.tvNotificationDescription
    val ivNotiIcon = view.ivNotiHistoryIcon
}
