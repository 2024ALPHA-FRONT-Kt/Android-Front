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
import com.android.myapplication.ui.knowledge_community.data_class.PostingKComment
import com.android.myapplication.ui.knowledge_community.data_class.ViewingKnowledge
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
    private lateinit var idOfPost: String

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

        binding.viewKnowledgePostAnswerEnterButton.setOnClickListener {
            val id = intent.getStringExtra("itemId").toString()
            val content = binding.viewKnowledgePostEnteringAnswer.text.toString()
            val postingKComment = PostingKComment(
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
                            val intent = Intent(
                                this@ViewKnowledgePostActivity,
                                ViewKnowledgePostWithAnswerActivity::class.java
                            )
                            intent.putExtra("postId", id)
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
                    idOfPost = data.userId

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
        } else {
            val fromWritePostId = intent.getStringExtra("itemIdWrite").toString()
            GlobalScope.launch(Dispatchers.IO) {
                Log.d("dmdkdmdkdk", "getPostId: $fromWritePostId")
                try {
                    val responseData =
                        apiService.knowledgePostDetail(token, fromWritePostId)
                    Log.d("dmddowfwefwfewfw", "responseData: $responseData")
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
                    Log.e("eadsfeqeerreqrror", e.toString())
                    e.printStackTrace()
                }
            }
            binding.viewKnowledgePostAnswerEnterButton.setOnClickListener {
                val id = intent.getStringExtra("itemId").toString()
                val content = binding.viewKnowledgePostEnteringAnswer.text.toString()
                val postingKComment = PostingKComment(
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
                                val intent = Intent(
                                    this@ViewKnowledgePostActivity,
                                    ViewKnowledgePostWithAnswerActivity::class.java
                                )
                                intent.putExtra("postId", fromWritePostId)
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
        }

        binding.viewKnowledgePostMenu.setOnClickListener {
            popup(binding.viewKnowledgePostMenu)
        }
    }

    private fun popup(v: View) {
        val popup = PopupMenu(this, v)
        popup.menuInflater.inflate(R.menu.communitiy_menu, popup.menu)

        if (idOfPost != userId) {
            popup.menu.findItem(R.id.menu_2).isVisible = false
            popup.menu.findItem(R.id.menu_3).isVisible = false
        }

        popup.setOnMenuItemClickListener { item ->
            clickMenu(item)
        }
        popup.show()
    }

    private fun clickMenu(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_1 -> {
                reportUser()
                true
            }
            R.id.menu_2 -> {
                true
            }
            R.id.menu_3 -> {
                true
            }
            else -> false
        }
    }

    private fun reportUser() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = apiService.report(token, idOfPost)
                Toast.makeText(this@ViewKnowledgePostActivity, "신고되었습니다.", Toast.LENGTH_SHORT)
                    .show()
                Log.e("dlsdikjfsldf", response.toString())
            } catch (e: Exception) {
                Log.e("error", "Error reporting user", e)
            }
        }
    }
}