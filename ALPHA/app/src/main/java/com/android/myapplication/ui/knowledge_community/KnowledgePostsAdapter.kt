package com.android.myapplication.ui.knowledge_community

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.myapplication.App
import com.android.myapplication.R
import com.android.myapplication.api.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class KnowledgePostsAdapter(private val items: ArrayList<KnowledgePosts>, private val onItemClick: (KnowledgePosts) -> Unit) : RecyclerView.Adapter<KnowledgePostsAdapter.ViewHolder>() {

    override fun getItemCount(): Int = items.size
    val apiService = RetrofitClient.apiservice
    val globalAccessToken: String = App.prefs.getItem("accessToken", "no Token")
    val token = "Bearer ${globalAccessToken.replace("\"", "")}"
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener { onItemClick(item) }
        GlobalScope.launch(Dispatchers.IO) {
            val page = 0
            val postType = "QNA" // 고정값
            val responseData = apiService.knowLedgeLists(token, postType, page)
            Log.d("dmd!!", responseData.toString())
        }
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

        fun bind(item: KnowledgePosts) {
            titleTextView.text = item.view_knowledge_posts_title
            contentTextView.text = item.view_knowledge_posts_content
        }
    }
}