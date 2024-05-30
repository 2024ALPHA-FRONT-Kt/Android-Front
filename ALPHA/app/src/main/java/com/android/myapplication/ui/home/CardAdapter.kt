package com.android.myapplication.ui.home

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.android.myapplication.R
import com.android.myapplication.ui.bootcamp.CampActivity
import com.android.myapplication.ui.disc.DiscActivity
import com.android.myapplication.ui.free_community.FreePostListActivity
import com.android.myapplication.ui.knowledge_community.KnowledgePostListActivity

class CardAdapter(var cardImage: ArrayList<Int>) :
    RecyclerView.Adapter<CardAdapter.PagerViewHolder>() {


    inner class PagerViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder
        (
        LayoutInflater.from(parent.context).inflate(R.layout.viewpager_slide_main, parent, false)
    ) {
        val cards = itemView.findViewById<ImageView>(R.id.card)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PagerViewHolder((parent))

    override fun getItemCount(): Int = cardImage.size

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.cards.setImageResource(cardImage[position])

        holder.cards.setOnClickListener {
            if (position == 1) {
                val intent = Intent(holder.itemView.context, DiscActivity::class.java)
                ContextCompat.startActivity(holder.itemView.context, intent, null)
            } else if (position == 0) {
                val intent = Intent(holder.itemView.context, CampActivity::class.java)
                ContextCompat.startActivity(holder.itemView.context, intent, null)
            } else {
                // Dialog만들기
                val mDialogView = LayoutInflater.from(holder.itemView.context).inflate(R.layout.popup_select_community, null)
                val mBuilder = AlertDialog.Builder(holder.itemView.context)
                    .setView(mDialogView)
                mBuilder.show()

                val free = mDialogView.findViewById<TextView>(R.id.free)
                free.setOnClickListener {
                    val intent = Intent(holder.itemView.context, FreePostListActivity::class.java)
                    ContextCompat.startActivity(holder.itemView.context, intent, null)
                }
                val know = mDialogView.findViewById<TextView>(R.id.know)
                know.setOnClickListener {
                    val intent = Intent(holder.itemView.context, KnowledgePostListActivity::class.java)
                    ContextCompat.startActivity(holder.itemView.context, intent, null)
                }
            }
        }
    }
}