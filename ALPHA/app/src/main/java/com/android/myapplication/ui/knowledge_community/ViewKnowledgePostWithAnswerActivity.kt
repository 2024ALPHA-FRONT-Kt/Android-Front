package com.android.myapplication.ui.knowledge_community

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.myapplication.App
import com.android.myapplication.api.RetrofitClient
import com.android.myapplication.databinding.ActivityViewKnowledgePostWithAnswerBinding
import com.android.myapplication.ui.knowledge_community.data_class.ResponseCommentDto
import com.android.myapplication.ui.knowledge_community.data_class.ViewingKnowledge
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewKnowledgePostWithAnswerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewKnowledgePostWithAnswerBinding
    private val apiService = RetrofitClient.apiservice
    private val gson = Gson()
    private val globalAccessToken: String = App.prefs.getItem("accessToken", "no Token")
    private val token = "Bearer ${globalAccessToken.replace("\"", "")}"
    private val id: String = App.prefs.getItem("userId", "noID")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewKnowledgePostWithAnswerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val isFromWriteActivity = intent.getBooleanExtra("isFromWriteActivity", false)
        if (isFromWriteActivity) {
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val responseData = apiService.knowledgePostDetail(token, "dmd")
                    Log.d("QNdkd", responseData.toString())
                    val jsonObject = gson.fromJson(responseData.data.toString(), JsonObject::class.java)
                    val data = gson.fromJson(jsonObject, ViewingKnowledge::class.java)
                    val userEmail = data.email.split("@")[0]
                    Log.d("dmddo", data.toString())

                    withContext(Dispatchers.Main) {
                        binding.viewKnowledgePostWithTitle.text = data.title
                        binding.viewKnowledgePostWithContent.text = data.content
                        binding.viewKnowledgePostWithUserId.text = "${data.univ} $userEmail"
                        binding.knowledgePostViewersCount.text = data.views.toString()

                        val comment = if (data.responseCommentDto.isNotEmpty()) {
                            data.responseCommentDto[0]
                        } else {
                            ResponseCommentDto("User", "University", "@", "선배님들의 답변을 기다려 보아요!")
                        }

                        Log.d("fnflf", "Comment: $comment")

                        binding.viewKnowledgePostWithSchOfAnswer.text = comment.univ
                        binding.viewKnowledgePostWithDptOfAnswer.text = comment.email
                        binding.viewKnowledgePostWithAnswerContent.text = comment.content

                        Log.d("aespatl", "UI 업데이트 완료")
                    }
                } catch (e: Exception) {
                    Log.e("ViewKnowledgePostWithAnswerActivity", e.toString())
                }
            }
        }

        val isFromListAnswer = intent.getBooleanExtra("isFromListAnswer", false)
        val fromListPostId = intent.getStringExtra("itemId").toString()
        if (isFromListAnswer) {
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val responseData = apiService.knowledgePostDetail(token, fromListPostId)
                    Log.d("QNdkd", responseData.toString())
                    val jsonObject = gson.fromJson(responseData.data.toString(), JsonObject::class.java)
                    val data = gson.fromJson(jsonObject, ViewingKnowledge::class.java)
                    val userEmail = data.email.split("@")[0]
                    Log.d("dmddo", data.toString())

                    withContext(Dispatchers.Main) {
                        binding.viewKnowledgePostWithTitle.text = data.title
                        binding.viewKnowledgePostWithContent.text = data.content
                        binding.viewKnowledgePostWithUserId.text = "${data.univ} $userEmail"
                        binding.knowledgePostViewersCount.text = data.views.toString()

                        val comment = if (data.responseCommentDto.isNotEmpty()) {
                            data.responseCommentDto[0]
                        } else {
                            ResponseCommentDto("User", "University", "@", "선배님들의 답변을 기다려 보아요!")
                        }

                        Log.d("fnflf", "Comment: $comment")

                        binding.viewKnowledgePostWithSchOfAnswer.text = comment.univ
                        binding.viewKnowledgePostWithDptOfAnswer.text = comment.email
                        binding.viewKnowledgePostWithAnswerContent.text = comment.content

                        Log.d("aespatl", "UI 업데이트 완료")
                    }
                } catch (e: Exception) {
                    Log.e("ViewKnowledgePostWithAnswerActivity", e.toString())
                }
            }
        }

        binding.writeKnowledgePostBackButton.setOnClickListener {
            intent = Intent(this, KnowledgePostListActivity::class.java)
            startActivity(intent)
            finish()
        }

        val itemId = intent.getStringExtra("itemId")
        if (itemId != null) {
            fetchKnowledgePostDetail(itemId)
        } else {
            Log.e("Item", "아이템 없음")
        }
    }


    private fun fetchKnowledgePostDetail(itemId: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val responseData = apiService.knowledgePostDetail(token, itemId)
                Log.d("dmddo", responseData.toString())
                val jsonObject = gson.fromJson(responseData.data.toString(), JsonObject::class.java)
                val data = gson.fromJson(jsonObject, ViewingKnowledge::class.java)
                val userEmail = data.email.split("@")[0]
                Log.d("dmddo", data.toString())

                withContext(Dispatchers.Main) {
                    binding.viewKnowledgePostWithTitle.text = data.title
                    binding.viewKnowledgePostWithContent.text = data.content
                    binding.viewKnowledgePostWithUserId.text = "${data.univ} $userEmail"
                    binding.knowledgePostViewersCount.text = data.views.toString()

                    val comment = if (data.responseCommentDto.isNotEmpty()) {
                        data.responseCommentDto[0]
                    } else {
                        ResponseCommentDto("User", "University", "@", "선배님들의 답변을 기다려 보아요!")
                    }

                    Log.d("dmddo", "Comment: $comment")

                    binding.viewKnowledgePostWithSchOfAnswer.text = comment.univ
                    binding.viewKnowledgePostWithDptOfAnswer.text = comment.email
                    binding.viewKnowledgePostWithAnswerContent.text = comment.content

                    Log.d("dmddo", "UI 업데이트 완료")
                }
            } catch (e: Exception) {
                Log.e("ViewKnowledgePostWithAnswerActivity", e.toString())
            }
        }
    }
}
