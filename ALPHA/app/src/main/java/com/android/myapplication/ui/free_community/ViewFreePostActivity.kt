package com.android.myapplication.ui.free_community

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.myapplication.App
import com.android.myapplication.api.RetrofitClient
import com.android.myapplication.databinding.ActivityViewFreePostBinding
import com.android.myapplication.ui.free_community.data_class.ViewingFree
import com.android.myapplication.ui.knowledge_community.ViewFreePostPlusActivity
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewFreePostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewFreePostBinding
    private val apiService = RetrofitClient.apiservice
    private val gson = Gson()
    private val globalAccessToken: String = App.prefs.getItem("accessToken", "no Token")
    private val token = "Bearer ${globalAccessToken.replace("\"", "")}"
    private lateinit var postId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewFreePostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val isFromFreeList = intent.getBooleanExtra("isFromFreeList", false)
        if (isFromFreeList) {
            GlobalScope.launch(Dispatchers.IO) {
                val postId = intent.getStringExtra("fromListPostId").toString()
                val responseData = apiService.freePostDetail(token, postId)
                Log.e("From Free List View Post", responseData.toString())
                val jsonObject =
                    gson.fromJson(responseData.data.toString(), JsonObject::class.java)
                val data = gson.fromJson(jsonObject, ViewingFree::class.java)
                val uId = data.email.split("@")[0]
                withContext(Dispatchers.Main) {
                    binding.viewFreePostTitle.text = data.title
                    binding.viewFreePostUserSch.text = data.univ
                    binding.viewFreePostUserId.text = uId
                    binding.freePostViewersCount.text = data.views.toString()
                    binding.freePostReadingContent.text = data.content
                    binding.freePostRecommend.text = data.likeNumber.toString()
                    binding.freePostComment.text = data.commentNumber.toString()
                }
            }
        } else {
            GlobalScope.launch(Dispatchers.IO) {
                val postId = intent.getStringExtra("fromWriteFreePostId").toString()
                val responseData = apiService.freePostDetail(token, postId)
                Log.e("From Free Write Post", responseData.toString())
                val jsonObject =
                    gson.fromJson(responseData.data.toString(), JsonObject::class.java)
                val data = gson.fromJson(jsonObject, ViewingFree::class.java)
                val uId = data.email.split("@")[0]
                withContext(Dispatchers.Main) {
                    binding.viewFreePostTitle.text = data.title
                    binding.viewFreePostUserSch.text = data.univ
                    binding.viewFreePostUserId.text = uId
                    binding.freePostViewersCount.text = data.views.toString()
                    binding.freePostReadingContent.text = data.content
                    binding.freePostRecommend.text = data.likeNumber.toString()
                    binding.freePostComment.text = data.commentNumber.toString()
                }
            }
        }

        binding.freePostCommentPlusButton.setOnClickListener {
            intent = Intent(this, ViewFreePostPlusActivity::class.java)
            intent.putExtra("fromViewPostId", postId)
        }

        binding.viewFreePostMenu.setOnClickListener {
            // todo
        }
    }
}