package com.android.myapplication.ui.knowledge_community

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.myapplication.App
import com.android.myapplication.R
import com.android.myapplication.api.RetrofitClient
import com.android.myapplication.databinding.ActivityViewKnowledgePostBinding
import com.android.myapplication.ui.knowledge_community.data_class.ViewingKnowledge
import com.android.myapplication.ui.knowledge_community.data_class.postingKComment
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewKnowledgePostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewKnowledgePostBinding
    private val apiService = RetrofitClient.apiservice
    private val gson = Gson()
    private val globalAccessToken: String = App.prefs.getItem("accessToken", "no Token")
    private val token = "Bearer ${globalAccessToken.replace("\"", "")}"
    private val userId: String = App.prefs.getItem("userId", "noID")

    private lateinit var postId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewKnowledgePostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.viewKnowledgePostBackButton.setOnClickListener {
            intent = Intent(this, KnowledgePostListActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.viewKnowledgePostAnswerEnterButton.setOnClickListener { // 답변하기
            val id = intent.getStringExtra("itemId").toString()
            val content = binding.viewKnowledgePostEnteringAnswer.text.toString()
            val postingKComment = postingKComment(
                postId = id,
                content = content
            )
            Log.d("akwdk", "postingKComment: $postingKComment")

            if (content.isNotBlank()) {
                GlobalScope.launch(Dispatchers.IO) {
                    try {
                        val responsData = apiService.postingKComment(token, postingKComment)
                        Log.d("akwdk", "responsData: $responsData")
                        withContext(Dispatchers.Main) {
                            val intent = Intent(this@ViewKnowledgePostActivity, ViewKnowledgePostWithAnswerActivity::class.java)
                            intent.putExtra("postId", id.toString())
                            intent.putExtra("isFromAnswering", true)
                            startActivity(intent)
                            finish()
                        }
                    } catch (e: Exception) {
                        Log.e("error", "Error posting comment", e)
                    }
                }
            } else {
                Toast.makeText(this, "답변 내용을 입력해 주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        val isFromKList = intent.getBooleanExtra("isFromKList", false)
        if (isFromKList) {
            GlobalScope.launch(Dispatchers.IO) {
                val getPostId = intent.getStringExtra("itemId").toString()
                Log.d("dmdkdmdkdk", "getPostId: $getPostId")
                try {
                    val responseData = apiService.knowledgePostDetail(token, getPostId)
                    Log.d("dmddo", "responseData: $responseData")
                    val jsonObject =
                        gson.fromJson(responseData.data.toString(), JsonObject::class.java)
                    val data = gson.fromJson(jsonObject, ViewingKnowledge::class.java)
                    val userEmail = data.email.split("@")[0]
                    Log.d("dmddo", "ViewingKnowledge: $data")

                    withContext(Dispatchers.Main) {
                        binding.viewKnowledgePostTitle.text = data.title
                        binding.viewKnowledgePostContent.text = data.content
                        binding.viewKnowledgePostUserId.text = "${data.univ} $userEmail"
                        binding.knowledgePostViewersCount.text = data.views.toString()

                        Log.d("dmddo", "UI 업데이트 완료")
                    }
                } catch (e: Exception) {
                    Log.e("ViewKnowledgePostWithAnswerActivity", "Error fetching post detail", e)
                }
            }
            fetchKnowledgePostDetail()
        }

        binding.viewKnowledgePostMenu.setOnClickListener {
            popup(binding.viewKnowledgePostMenu)
        }
    }

    private fun fetchKnowledgePostDetail() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val responseData = apiService.knowledgePostDetail(token, postId)
                Log.d("dmddo", "responseData: $responseData")
                val jsonObject = gson.fromJson(responseData.data.toString(), JsonObject::class.java)
                val data = gson.fromJson(jsonObject, ViewingKnowledge::class.java)
                val userEmail = data.email.split("@")[0]
                Log.d("dmddo", "ViewingKnowledge: $data")

                withContext(Dispatchers.Main) {
                    binding.viewKnowledgePostUserId.text = "${data.univ} $userEmail"
                    binding.viewKnowledgePostTitle.text = data.title
                    binding.viewKnowledgePostContent.text = data.content
                    binding.knowledgePostViewersCount.text = data.views.toString()
                }
            } catch (e: Exception) {
                Log.e("ViewKnowledgePostActivity", "Error fetching post detail", e)
            }
        }
    }


    private fun popup(v: View) {
        val popup = PopupMenu(this, v)
        popup.menuInflater.inflate(R.menu.k_plus_q_menu, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            clickMenu(item)
        }
        popup.show()
    }

    private fun clickMenu(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.k_plus_q_menu_3 -> {
                GlobalScope.launch(Dispatchers.IO) {
                    try {
                        val responseData = apiService.knowledgePostDetail(token, "556ba748-d8b4-4258-8333-4497697a1a67")
                        val jsonObject = gson.fromJson(responseData.data.toString(), JsonObject::class.java)
                        val data = gson.fromJson(jsonObject, ViewingKnowledge::class.java)
                        val editTitleDraft = data.title
                        val editContentDraft = data.content

                        withContext(Dispatchers.Main) {
                            val intent = Intent(this@ViewKnowledgePostActivity, WriteKnowledgePostActivity::class.java).apply {
                                putExtra("editTitleDraft", editTitleDraft)
                                putExtra("editContentDraft", editContentDraft)
                                putExtra("isFromWriteActivity", true)
                            }
                            startActivity(intent)
                        }
                    } catch (e: Exception) {
                        Log.e("menuerror1", "Error fetching post detail", e)
                    }
                }
                true
            }
            else -> false
        }
    }
}