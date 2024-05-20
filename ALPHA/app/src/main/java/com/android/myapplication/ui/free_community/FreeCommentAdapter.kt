package com.android.myapplication.ui.free_community

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.myapplication.R

class FreeCommentAdapter(private val items: ArrayList<FreeComments>) : RecyclerView.Adapter<FreeCommentAdapter.ViewHolder>() {

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
        val inflatedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_free_post_comments, parent, false)
        return ViewHolder(inflatedView)
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val schTextView: TextView = itemView.findViewById(R.id.free_post_comment_sch)
        private val idTextView: TextView = itemView.findViewById(R.id.free_post_comment_user_id)
        private val contentTextView: TextView =
            itemView.findViewById(R.id.free_post_comment_content)

        fun bind(listener: View.OnClickListener, item: FreeComments) {
            schTextView.text = item.view_free_comment_sch
            idTextView.text = item.view_free_comment_id
            contentTextView.text = item.view_free_comment_content
            itemView.setOnClickListener(listener)
        }
    }
}