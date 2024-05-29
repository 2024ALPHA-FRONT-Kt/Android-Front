package com.android.myapplication.ui.knowledge_community

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.myapplication.App
import com.android.myapplication.R
import com.android.myapplication.api.RetrofitClient
import com.android.myapplication.ui.knowledge_community.data_class.PostList
import com.android.myapplication.ui.knowledge_community.data_class.PostingKnowledge
import kotlinx.coroutines.launch

class KnowledgePostsAdapter(private val items: MutableList<PostList>) : RecyclerView.Adapter<KnowledgePostsAdapter.ViewHolder>() {

    override fun getItemCount(): Int = items.size
    val apiService = RetrofitClient.apiservice
    val globalAccessToken: String = App.prefs.getItem("accessToken", "no Token")
    val token = "Bearer ${globalAccessToken.replace("\"", "")}"
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            val postId = item.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.item_knowledge_posts, parent, false)
        return ViewHolder(inflatedView)
    }

    fun addPosts(newPosts: List<PostList>) {
        items.addAll(newPosts)
        notifyDataSetChanged()
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val titleTextView: TextView = v.findViewById(R.id.view_knowledge_posts_title)
        private val contentTextView: TextView = v.findViewById(R.id.view_knowledge_post_content)

        fun bind(item: PostList) {
            titleTextView.text = item.title
            contentTextView.text = item.content
        }
    }
}