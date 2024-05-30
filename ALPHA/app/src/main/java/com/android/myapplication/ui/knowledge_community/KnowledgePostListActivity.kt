package com.android.myapplication.ui.knowledge_community

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.myapplication.App
import com.android.myapplication.api.RetrofitClient
import com.android.myapplication.databinding.ActivityKnowledgePostListBinding
import com.android.myapplication.ui.knowledge_community.data_class.PostList
import com.google.gson.Gson
import com.google.gson.JsonArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class KnowledgePostListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKnowledgePostListBinding
    private val apiService = RetrofitClient.apiservice
    private val gson = Gson()
    private val globalAccessToken: String = App.prefs.getItem("accessToken", "no Token")
    private val token = "Bearer ${globalAccessToken.replace("\"", "")}"
    private val id: String = App.prefs.getItem("userId", "noID")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKnowledgePostListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val recyclerView = binding.knowledgeListView
        recyclerView.layoutManager = LinearLayoutManager(this)

        binding.knowledgePostsListWritingButton.setOnClickListener {
            val intent = Intent(this, WriteKnowledgePostActivity::class.java)
            startActivity(intent)
        }

        GlobalScope.launch(Dispatchers.IO) {
            val page = 0
            val postType = "QNA" // 고정값
            val responseData = apiService.knowLedgeLists(token, postType, page)
            Log.d("dmd!!", responseData.data.toString())
            val ddd = gson.fromJson(responseData.data.toString(), JsonArray::class.java)

            val mList: MutableList<PostList> = mutableListOf()

            for (i in ddd){
                val postObject = i.asJsonObject
                val id = postObject.get("id").asString
                val content = postObject.get("content").asString
                val title = postObject.get("title").asString
                mList.add(PostList(id,content,title))
            }

            binding.root.post{
                recyclerView.adapter = KnowledgePostsAdapter(mList)
            }

        }

    }
}