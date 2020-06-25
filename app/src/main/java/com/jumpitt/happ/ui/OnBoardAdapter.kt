package com.jumpitt.happ.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jumpitt.happ.R
import com.jumpitt.happ.model.OnBoardSlide
import com.jumpitt.happ.utils.ColorIdResource
import com.jumpitt.happ.utils.Labelstext
import com.jumpitt.happ.utils.containedStyle

class OnBoardAdapter (private val introSlides: List<OnBoardSlide>): RecyclerView.Adapter<OnBoardAdapter.OnBoardViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardViewHolder {
        return OnBoardViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_onboard_container,parent,false))
    }

    override fun getItemCount(): Int {
        return introSlides.size
    }

    override fun onBindViewHolder(holder: OnBoardViewHolder, position: Int) {
        holder.bind(introSlides[position])
    }


    inner class OnBoardViewHolder(view: View): RecyclerView.ViewHolder(view){

        private val image = view.findViewById<ImageView>(R.id.ivOnBoardIcon)
        private val title = view.findViewById<TextView>(R.id.tvOnBoardTitle)
        private val description = view.findViewById<TextView>(R.id.tvOnBoardDescription)

        fun bind(onBoardSlide: OnBoardSlide){
            title.containedStyle(Labelstext.H3, ColorIdResource.BLACK, font = R.font.dmsans_medium)
            description.containedStyle(Labelstext.BODY1, ColorIdResource.BLACK, 0.87F)
            image.setImageResource(onBoardSlide.icon)
            title.text = onBoardSlide.title
            description.text = onBoardSlide.description
        }
    }

}