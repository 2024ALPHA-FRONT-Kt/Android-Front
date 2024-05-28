package com.android.myapplication.ui.free_community

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.myapplication.App
import com.android.myapplication.api.RetrofitClient
import com.android.myapplication.databinding.ActivityFreePostListBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FreePostListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFreePostListBinding
    private lateinit var adapter: FreePostAdapter
    private var currentPage = 0

    private val apiService = RetrofitClient.apiservice
    private val gson = Gson()
    private val globalAccessToken: String = App.prefs.getItem("accessToken", "no Token")
    private val token = "Bearer ${globalAccessToken.replace("\"", "")}"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFreePostListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = FreePostAdapter(arrayListOf())

        binding.freeListView.layoutManager = LinearLayoutManager(this)
        binding.freeListView.adapter = adapter

        binding.freeListView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                if (layoutManager.findLastCompletelyVisibleItemPosition() == adapter.itemCount - 1) {
                    currentPage++
                    loadPosts(currentPage)
                }
            }
        })
        loadPosts(currentPage)
    }

    private fun loadPosts(page: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.freeLists(
                    token,
                    "FREE",
                    page
                )
                val dataJson = gson.toJson(response.data)
                val type = object : TypeToken<List<FreePosts>>() {}.type
                val newPosts: List<FreePosts> = gson.fromJson(dataJson, type)
                CoroutineScope(Dispatchers.Main).launch {
                    adapter.addPosts(newPosts)
                }
            } catch (e: Exception) {
                // todo 예외 처리
            }
        }
    }
}
