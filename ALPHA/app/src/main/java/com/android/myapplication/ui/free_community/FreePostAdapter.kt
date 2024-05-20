package com.example.xptmxmeksid

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.myapplication.R
import com.android.myapplication.ui.free_community.FreePosts

class FreePostAdapter(private val items: ArrayList<FreePosts>) : RecyclerView.Adapter<FreePostAdapter.ViewHolder>() {

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val listener = View.OnClickListener {
            // todo
        }
        holder.apply {
            bind(listener, item)
            itemView.tag = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.item_free_posts, parent, false)
        return ViewHolder(inflatedView)
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        private val titleTextView: TextView = view.findViewById(R.id.view_free_list_title)
        private val contentTextView: TextView = view.findViewById(R.id.view_free_list_content)

        fun bind(listener: View.OnClickListener, item: FreePosts) {
            titleTextView.text = item.view_free_list_title
            contentTextView.text = item.view_free_list_content
            view.setOnClickListener(listener)
        }
    }
}