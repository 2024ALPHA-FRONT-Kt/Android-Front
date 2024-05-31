package com.android.myapplication.ui.knowledge_community

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.myapplication.App
import com.android.myapplication.R
import com.android.myapplication.api.RetrofitClient
import com.android.myapplication.ui.knowledge_community.data_class.PostList
import com.android.myapplication.ui.knowledge_community.data_class.ViewingKnowledge
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class KnowledgePostsAdapter(private val items: MutableList<PostList>) : RecyclerView.Adapter<KnowledgePostsAdapter.ViewHolder>() {

    override fun getItemCount(): Int = items.size
    private val apiService = RetrofitClient.apiservice
    private val gson = Gson()
    private val globalAccessToken: String = App.prefs.getItem("accessToken", "no Token")
    private val token = "Bearer ${globalAccessToken.replace("\"", "")}"

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val responseData = apiService.knowledgePostDetail(token, item.id)
                    val jsonObject = gson.fromJson(responseData.data.toString(), JsonObject::class.java)
                    val data = gson.fromJson(jsonObject, ViewingKnowledge::class.java)
                    Log.d("whyrano", "12345")

                    withContext(Dispatchers.Main) {
                        Log.d("whyrano", "123456789")
                        val intent = if (data.responseCommentDto.isNotEmpty()) {
                            Intent(context, ViewKnowledgePostWithAnswerActivity::class.java).apply {
                                putExtra("itemId", item.id)
                                putExtra("isFromListAnswer", true)
                            }
                        } else {
                            Intent(context, ViewKnowledgePostActivity::class.java).apply {
                                putExtra("itemId", item.id)
                                putExtra("itemTitle", item.title)
                                putExtra("itemContent", item.content)
                                putExtra("isFromKList", true)
                            }
                        }
                        Log.d("whyrano", intent.toString())
                        context.startActivity(intent)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "게시글을 불러오는 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.item_knowledge_posts, parent, false)
        return ViewHolder(inflatedView)
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