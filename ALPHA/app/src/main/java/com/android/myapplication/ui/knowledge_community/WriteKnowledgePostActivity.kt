package com.android.myapplication.ui.knowledge_community

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.myapplication.App
import com.android.myapplication.api.RetrofitClient
import com.android.myapplication.databinding.ActivityWriteKnowledgePostBinding
import com.android.myapplication.ui.knowledge_community.data_class.EditingKnowledge
import com.android.myapplication.ui.knowledge_community.data_class.PostingKnowledge
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WriteKnowledgePostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWriteKnowledgePostBinding
    private val apiService = RetrofitClient.apiservice
    private val gson = Gson()
    private val globalAccessToken: String = App.prefs.getItem("accessToken", "no Token")
    private val token = "Bearer ${globalAccessToken.replace("\"", "")}"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWriteKnowledgePostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val fromEdit = intent.getBooleanExtra("fromEdit", false)
        if (fromEdit) {
            val title = intent.getStringExtra("title").toString()
            val content = intent.getStringExtra("content").toString()
            val postId = intent.getStringExtra("postId").toString()

            binding.writingKnowledgePostTitle.setText(title)
            binding.writingKnowledgePostBody.setText(content)

            binding.writingKnowledgePostUpload.setOnClickListener {
                val newTitle = binding.writingKnowledgePostTitle.text.toString()
                val newContent = binding.writingKnowledgePostBody.text.toString()

                if (newTitle.isBlank() || newContent.isBlank()) {
                    runOnUiThread {
                        when {
                            newTitle.isBlank() && newContent.isNotBlank() -> {
                                Toast.makeText(this, "제목을 입력해주세요.", Toast.LENGTH_SHORT).show()
                            }

                            newTitle.isNotBlank() && newContent.isBlank() -> {
                                Toast.makeText(this, "내용을 입력해주세요.", Toast.LENGTH_SHORT).show()
                            }

                            newTitle.isBlank() && newContent.isBlank() -> {
                                Toast.makeText(this, "제목과 내용을 입력해주세요.", Toast.LENGTH_SHORT).show()
                            }

                            else -> {
                                val editKnowledge = EditingKnowledge(
                                    id = postId,
                                    title = newTitle,
                                    content = newContent,
                                    image = null,
                                    postType = "QNA" // 고정
                                )

                                GlobalScope.launch(Dispatchers.IO) {
                                    try {
                                        val responseData =
                                            apiService.editKnowledge(token, editKnowledge)
                                    } catch (e: Exception) {
                                        Log.e("wn", e.toString())
                                    }
                                    withContext(Dispatchers.Main) {
                                        val intent = Intent(
                                            this@WriteKnowledgePostActivity,
                                            ViewKnowledgePostActivity::class.java
                                        ).apply {
                                            putExtra("editPostId", postId)
                                            putExtra("fromEditWrite", true)
                                        }
                                        startActivity(intent)
                                        finish()
                                    }
                                }
                            }
                        }
                    }
                }
            }

        } else {
            binding.writingKnowledgePostUpload.setOnClickListener {
                val title = binding.writingKnowledgePostTitle.text.toString()
                val body = binding.writingKnowledgePostBody.text.toString()

                when {
                    title.isBlank() && body.isNotBlank() -> {
                        Toast.makeText(this, "제목을 입력해주세요.", Toast.LENGTH_SHORT).show()
                    }

                    title.isNotBlank() && body.isBlank() -> {
                        Toast.makeText(this, "내용을 입력해주세요.", Toast.LENGTH_SHORT).show()
                    }

                    title.isBlank() && body.isBlank() -> {
                        Toast.makeText(this, "제목과 내용을 입력해주세요.", Toast.LENGTH_SHORT).show()
                    }

                    else -> {
                        val postingKnowledge = PostingKnowledge(
                            title = title,
                            content = body,
                            image = null,
                            postType = "QNA"
                        )
                        GlobalScope.launch(Dispatchers.IO) {
                            try {
                                val responseData =
                                    apiService.postingKnowledgePost(token, postingKnowledge)
                                val responseJson = gson.toJson(responseData)
                                val jsonObject = gson.fromJson(responseJson, JsonObject::class.java)
                                val getPostId = jsonObject.get("data").asString
                                Log.d("fhrmzot", responseData.toString())
                                Log.d("intentData", "itemId: $getPostId, isFromWriteAc: true")
                                withContext(Dispatchers.Main) {
                                    val intent = Intent(
                                        this@WriteKnowledgePostActivity,
                                        ViewKnowledgePostActivity::class.java
                                    ).apply {
                                        putExtra("itemIdWrite", getPostId.toString())
                                        putExtra("isFromWriteAc", true)
                                    }
                                    startActivity(intent)
                                    finish()
                                }
                            } catch (e: Exception) {
                                Log.d("error", e.toString())
                            }
                        }
                    }
                }
            }
        }

        binding.writingKnowledgePostCancel.setOnClickListener {
            val intent = Intent(this, KnowledgePostListActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}