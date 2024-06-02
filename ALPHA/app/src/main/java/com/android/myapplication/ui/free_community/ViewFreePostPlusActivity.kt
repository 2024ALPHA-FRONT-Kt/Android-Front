package com.android.myapplication.ui.free_community

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.myapplication.App
import com.android.myapplication.R
import com.android.myapplication.api.RetrofitClient
import com.android.myapplication.databinding.ActivityViewFreePostPlusBinding
import com.android.myapplication.ui.free_community.data_class.CommentList
import com.android.myapplication.ui.free_community.data_class.PostingComment
import com.android.myapplication.ui.free_community.data_class.ViewingFree
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
    private lateinit var postId: String

    private val recommending = R.drawable.ic_free_post_recommend
    private val cancelRecommend = R.drawable.ic_free_recommend_after
    private var isOriginalImage = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewFreePostPlusBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val recyclerView = binding.freeCommentView
        recyclerView.layoutManager = LinearLayoutManager(this)

        postId = intent.getStringExtra("fromViewPostId").toString()
        loadPostDetails()

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
                        val responseData = apiService.postingFComment(token, postingFComment)
                        withContext(Dispatchers.Main) {
                            loadPostDetails()
                        }
                    } catch (e: Exception) {
                        Log.e("error", "Error posting comment", e)
                    }
                }
            }
        }
    }

    private fun loadPostDetails() {
        GlobalScope.launch(Dispatchers.IO) {
            val responseData = apiService.freePostDetail(token, postId)
            Log.e("From Free View Post", responseData.toString())
            val jsonObject =
                gson.fromJson(responseData.data.toString(), JsonObject::class.java)
            val commentLists: MutableList<CommentList> = mutableListOf()
            val responseComments = jsonObject.getAsJsonArray("responseCommentDto")

            for (commentJson in responseComments) {
                val commentObject = commentJson.asJsonObject
                val univ = commentObject.get("univ").asString
                val email = commentObject.get("email").asString
                val content = commentObject.get("content").asString
                commentLists.add(CommentList(email, postId, univ, content))
            }

            withContext(Dispatchers.Main) {
                binding.root.post {
                    binding.freeCommentView.adapter = FreeCommentAdapter(commentLists)
                    val data = gson.fromJson(jsonObject, ViewingFree::class.java)
                    val uId = data.email.split("@")[0]
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
    }
}
