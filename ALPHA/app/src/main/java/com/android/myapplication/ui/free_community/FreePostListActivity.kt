package com.android.myapplication.ui.free_community

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.myapplication.App
import com.android.myapplication.api.RetrofitClient
import com.android.myapplication.databinding.ActivityFreePostListBinding
import com.android.myapplication.ui.free_community.data_class.PostList
import com.google.gson.Gson
import com.google.gson.JsonArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FreePostListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFreePostListBinding
    private val apiService = RetrofitClient.apiservice
    private val gson = Gson()
    private val globalAccessToken: String = App.prefs.getItem("accessToken", "no Token")
    private val token = "Bearer ${globalAccessToken.replace("\"", "")}"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFreePostListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val recyclerView = binding.freeListView
        recyclerView.layoutManager = LinearLayoutManager(this)

        binding.freePostsListWritingButton.setOnClickListener {
            val intent = Intent(this, WriteFreePostActivity::class.java)
            startActivity(intent)
        }

        GlobalScope.launch(Dispatchers.IO) {
            val page = 0
            val postType = "FREE" // 고정값
            val responseData = apiService.freeLists(token, postType, page)
            Log.d("dmd!!", responseData.data.toString())
            val datas = gson.fromJson(responseData.data.toString(), JsonArray::class.java)

            val mList: MutableList<PostList> = mutableListOf()

            for (i in datas) {
                val postObject = i.asJsonObject
                val id = postObject.get("id").asString
                val content = postObject.get("content").asString
                val title = postObject.get("title").asString
                mList.add(PostList(id, content, title))
            }

            binding.root.post {
                recyclerView.adapter = FreePostAdapter(mList)
            }
        }
    }
}