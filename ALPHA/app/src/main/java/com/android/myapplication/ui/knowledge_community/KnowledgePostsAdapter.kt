package com.android.myapplication.ui.knowledge_community

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.myapplication.R

class KnowledgePostsAdapter(private val items: ArrayList<KnowledgePosts>, private val onItemClick: (KnowledgePosts) -> Unit) : RecyclerView.Adapter<KnowledgePostsAdapter.ViewHolder>() {

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener { onItemClick(item) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.item_knowledge_posts, parent, false)
        return ViewHolder(inflatedView)
    }

    fun addPosts(newPosts: List<KnowledgePosts>) {
        items.addAll(newPosts)
        notifyDataSetChanged()
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val titleTextView: TextView = v.findViewById(R.id.view_knowledge_posts_title)
        private val contentTextView: TextView = v.findViewById(R.id.view_knowledge_post_content)
        private val dateTextView: TextView = v.findViewById(R.id.view_knowledge_post_date)

        fun bind(item: KnowledgePosts) {
            titleTextView.text = item.view_knowledge_posts_title
            contentTextView.text = item.view_knowledge_posts_content
            dateTextView.text = item.view_knowledge_posts_date
        }
    }
}