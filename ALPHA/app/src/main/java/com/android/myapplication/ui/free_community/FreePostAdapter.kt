package com.android.myapplication.ui.free_community

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.myapplication.App
import com.android.myapplication.R
import com.android.myapplication.api.RetrofitClient
import com.android.myapplication.ui.free_community.data_class.PostList
import com.android.myapplication.ui.free_community.data_class.ViewingFree
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FreePostAdapter(private val items: MutableList<PostList>) :
    RecyclerView.Adapter<FreePostAdapter.ViewHolder>() {

    override fun getItemCount(): Int = items.size
    val apiService = RetrofitClient.apiservice
    private val gson = Gson()
    val globalAccessToken: String = App.prefs.getItem("accessToken", "no Token")
    val token = "Bearer ${globalAccessToken.replace("\"", "")}"
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val responseData = apiService.freePostDetail(token, item.id)
                    val jsonObject =
                        gson.fromJson(responseData.data.toString(), JsonObject::class.java)
                    val data = gson.fromJson(jsonObject, ViewingFree::class.java)

                    withContext(Dispatchers.Main) {
                        Log.d("whyrano", "123456789")
                        val intent = Intent(context, ViewFreePostActivity::class.java).apply {
                            putExtra("fromListPostId", item.id)
                            putExtra("isFromFreeList", true)
                        }
                        context.startActivity(intent)
                    }
                } catch (e: Exception) {
                    Log.e("Free Adapter error", e.toString())
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_free_posts, parent, false)
        return ViewHolder(inflatedView)
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val titleTextView: TextView = v.findViewById(R.id.view_free_list_title)
        private val contentTextView: TextView = v.findViewById(R.id.view_free_list_content)

        fun bind(item: PostList) {
            titleTextView.text = item.title
            contentTextView.text = item.content
        }
    }
}