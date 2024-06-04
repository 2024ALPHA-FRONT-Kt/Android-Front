package com.android.myapplication.ui.knowledge_community

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
    private val userRole: String = App.prefs.getItem("userRole", "noUserRole")
    private lateinit var emailOfPost: String
    private lateinit var targetPostId: String
    private lateinit var targetTitle: String
    private lateinit var targetContent: String


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
        val fromEditWrite = intent.getBooleanExtra("fromEditWrite", false)
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
                    emailOfPost = data.email
                    targetPostId = data.postId
                    targetTitle = data.title
                    targetContent = data.content
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
        } else if (fromEditWrite) {
            val editPostId = intent.getStringExtra("editPostId").toString()
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val responseData =
                        apiService.knowledgePostDetail(token, editPostId)
                    val jsonObject =
                        gson.fromJson(responseData.data.toString(), JsonObject::class.java)
                    val data = gson.fromJson(jsonObject, ViewingKnowledge::class.java)
                    val userEmail = data.email.split("@")[0]
                    withContext(Dispatchers.Main) {
                        binding.viewKnowledgePostTitle.text = data.title
                        binding.viewKnowledgePostContent.text = data.content
                        binding.viewKnowledgePostUserId.text = "${data.univ} $userEmail"
                        binding.knowledgePostViewersCount.text = data.views.toString()
                    }
                } catch (e: Exception) {
                    Log.e("asf", e.toString())
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
                                intent.putExtra("postId", editPostId)
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
                    emailOfPost = data.email
                    targetPostId = data.postId
                    targetTitle = data.title
                    targetContent = data.content
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

                if (userRole == "HIGH") {
                    Toast.makeText(this, "대학생 회원만 답변할 수 있습니다.", Toast.LENGTH_SHORT).show()
                } else {
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
                val popupMenu = PopupMenu(this@ViewKnowledgePostActivity, it)
                popupMenu.menuInflater.inflate(R.menu.community_menu, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.menu_1 -> {
                            Toast.makeText(applicationContext, "신고되었습니다.", Toast.LENGTH_SHORT)
                                .show()
                            true
                        }

                        R.id.menu_2 -> {
                            Toast.makeText(applicationContext, "기능 개발 중입니다. . .", Toast.LENGTH_SHORT)
                                .show()
                            true
                        }

                        R.id.menu_3 -> {
                            Toast.makeText(applicationContext, "기능 개발 중입니다. . .", Toast.LENGTH_SHORT)
                                .show()
                            true
                        }

                        else -> false
                    }
                }
                popupMenu.show()
            }
        }
    }
}