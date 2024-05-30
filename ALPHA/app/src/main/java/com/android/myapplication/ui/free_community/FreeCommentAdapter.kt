package com.android.myapplication.ui.free_community

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.myapplication.App
import com.android.myapplication.R
import com.android.myapplication.api.RetrofitClient
import com.android.myapplication.ui.free_community.data_class.CommentList
import com.android.myapplication.ui.free_community.data_class.PostList

class FreeCommentAdapter(private val items: MutableList<CommentList>) : RecyclerView.Adapter<FreeCommentAdapter.ViewHolder>() {

    override fun getItemCount(): Int = items.size
    val apiService = RetrofitClient.apiservice
    val globalAccessToken: String = App.prefs.getItem("accessToken", "no Token")
    val token = "Bearer ${globalAccessToken.replace("\"", "")}"
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            //todo
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.item_free_post_comments, parent, false)
        return ViewHolder(inflatedView)
    }
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val univTextView: TextView = v.findViewById(R.id.free_post_comment_sch)
        private val idTextView: TextView = v.findViewById(R.id.free_post_comment_user_id)
        private val contentTextView: TextView = v.findViewById(R.id.free_post_comment_content)

        fun bind(item: CommentList) {
            univTextView.text = item.univ
            idTextView.text = item.userId
            contentTextView.text = item.content
        }
    }
}