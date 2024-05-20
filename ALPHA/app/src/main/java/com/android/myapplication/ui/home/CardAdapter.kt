package com.android.myapplication.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.android.myapplication.R

class CardAdapter(var cardImage: ArrayList<Int>) :
    RecyclerView.Adapter<CardAdapter.PagerViewHolder>() {


    inner class PagerViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder
        (LayoutInflater.from(parent.context).inflate(R.layout.viewpager_slide_main, parent, false)) {
        val cards = itemView.findViewById<ImageView>(R.id.card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PagerViewHolder((parent))

    override fun getItemCount(): Int = cardImage.size

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.cards.setImageResource(cardImage[position])
    }
}