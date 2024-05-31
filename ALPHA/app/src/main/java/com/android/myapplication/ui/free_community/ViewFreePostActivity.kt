package com.android.myapplication.ui.free_community

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.myapplication.App
import com.android.myapplication.api.RetrofitClient
import com.android.myapplication.databinding.ActivityViewFreePostBinding
import com.android.myapplication.ui.free_community.data_class.PostingComment
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
    private var existResponseComment: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewFreePostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val isFromFreeList = intent.getBooleanExtra("isFromFreeList", false)
        GlobalScope.launch(Dispatchers.IO) {
            postId = if (isFromFreeList) {
                intent.getStringExtra("fromListPostId").toString()
            } else {
                intent.getStringExtra("fromWriteFreePostId").toString()
            }

            val responseData = apiService.freePostDetail(token, postId)
            Log.e(
                if (isFromFreeList) "From Free List View Post" else "From Free Write Post",
                responseData.toString()
            )
            val jsonObject = gson.fromJson(responseData.data.toString(), JsonObject::class.java)
            val data = gson.fromJson(jsonObject, ViewingFree::class.java)
            val uId = data.email.split("@")[0]
            existResponseComment = data.responseCommentDto.size > 0

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

        binding.freePostCommentPlusButton.setOnClickListener {
            if (existResponseComment) {
                val intent = Intent(this, ViewFreePostPlusActivity::class.java)
                intent.putExtra("fromViewPostId", postId)
                intent.putExtra("fromViewPlustButton", true)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "불러올 수 있는 댓글이 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }


        binding.viewFreePostBackButton.setOnClickListener {
            val intent = Intent(this, FreePostListActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.freePostCommentEnter.setOnClickListener {
            val content = binding.freePostEnteringComment.text.toString()
            val postingFComment = PostingComment(
                postId = postId,
                content = content
            )
            if (content.isNotBlank()) {
                GlobalScope.launch(Dispatchers.IO) {
                    try {
                        val responsData = apiService.postingFComment(token, postingFComment)
                        withContext(Dispatchers.Main) {
                            val intent = Intent(
                                this@ViewFreePostActivity,
                                ViewFreePostPlusActivity::class.java
                            )
                            intent.putExtra("isFromViewFreePostID", postId)
                            intent.putExtra("isFromViewFree", true)
                            startActivity(intent)
                            finish()
                        }
                    } catch (e: Exception) {
                        Log.e("error", "Error posting comment", e)
                    }
                }
            }
        }

        binding.viewFreePostMenu.setOnClickListener {
            // todo
        }
    }
}