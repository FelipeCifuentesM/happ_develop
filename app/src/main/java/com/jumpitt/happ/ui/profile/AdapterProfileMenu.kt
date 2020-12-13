package com.jumpitt.happ.ui.profile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.jumpitt.happ.R
import com.jumpitt.happ.model.ProfileMenu
import com.jumpitt.happ.utils.ColorIdResource
import com.jumpitt.happ.utils.Labelstext
import com.jumpitt.happ.utils.containedStyle
import com.jumpitt.happ.utils.setSafeOnClickListener
import kotlinx.android.synthetic.main.item_profile_menu.view.*

class AdapterProfileMenu(private val context: Context, private val profileMenuListObject: ArrayList<ProfileMenu>, private val itemClickListener: ClickListener): RecyclerView.Adapter<OptionHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionHolder {
        return OptionHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_profile_menu, parent, false))
    }

    override fun getItemCount(): Int {
        return profileMenuListObject.size
    }

    override fun onBindViewHolder(holder: OptionHolder, position: Int) {
        holder.bindItems(profileMenuListObject[position], position,  context, itemClickListener)
    }

    interface ClickListener {
        fun itemClickProfileMenu(position: Int)
    }
}

class OptionHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val ivIconProfileMenu = itemView.ivIconProfileMenu
    val tvTitleProfileMenu = itemView.tvTitleProfileMenu
    val containerItemProfileMenu = itemView.containerItemProfileMenu
    val separatorProfileMenu = itemView.separatorProfileMenu

    fun bindItems(itemProfileMenu: ProfileMenu, position: Int, context: Context, itemClick: AdapterProfileMenu.ClickListener){

        tvTitleProfileMenu.containedStyle(Labelstext.BUTTON, ColorIdResource.PRIMARY, font = R.font.dmsans_bold)
        ivIconProfileMenu.setImageResource(itemProfileMenu.icon)
        ivIconProfileMenu.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary))
        tvTitleProfileMenu.text = context.resources.getString(itemProfileMenu.option)

        if(position == 0){
            separatorProfileMenu.visibility = View.GONE
        }

        containerItemProfileMenu.setSafeOnClickListener {
            itemClick.itemClickProfileMenu(position)
        }

    }

}