package com.android.myapplication.ui.knowledge_community

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import com.android.myapplication.App
import com.android.myapplication.R
import com.android.myapplication.api.RetrofitClient
import com.android.myapplication.databinding.ActivityViewFreePostPlusBinding
import com.android.myapplication.databinding.ActivityViewKnowledgePostBinding
import com.android.myapplication.ui.free_community.data_class.ViewingFree
import com.android.myapplication.ui.knowledge_community.data_class.ViewingKnowledge
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewFreePostPlusActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewFreePostPlusBinding
    private val apiService = RetrofitClient.apiservice
    private val gson = Gson()
    private val globalAccessToken: String = App.prefs.getItem("accessToken", "no Token")
    private val token = "Bearer ${globalAccessToken.replace("\"", "")}"
    private val id: String = App.prefs.getItem("userId", "noID")
    private val postId = "1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewFreePostPlusBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        freePlus()
    }

    private fun freePlus() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val responseData = apiService.freePostDetail(token, postId)
                Log.d("dmddo", responseData.toString())
                val jsonObject = gson.fromJson(responseData.data.toString(), JsonObject::class.java)
                val data = gson.fromJson(jsonObject, ViewingFree::class.java)
                val userEmail = data.email.split("@")[0]
                Log.d("dmddo", data.toString())

                withContext(Dispatchers.Main) {
                    binding.viewFreePostUserSch.text = data.univ
                    binding.viewFreePostUserId.text = userEmail
                    binding.viewFreePostTitle.text = data.title
                    binding.freePostReadingContent.text = data.content
                    binding.freePostViewersCount.text = data.views.toString()
                    binding.freePostRecommend.text = data.like.toString()
                    binding.freePostComment.text = data.commentNumber.toString()
                }
            } catch (e: Exception) {
                Log.e("ViewFreePlus", e.toString())
            }
        }
    }
}