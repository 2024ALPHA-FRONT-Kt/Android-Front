package com.android.myapplication.ui.free_community

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.myapplication.App
import com.android.myapplication.api.RetrofitClient
import com.android.myapplication.databinding.ActivityWriteFreePostBinding
import com.android.myapplication.ui.free_community.data_class.PostingFree
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject.NULL

class WriteFreePostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWriteFreePostBinding
    private val apiService = RetrofitClient.apiservice
    private val gson = Gson()
    private val globalAccessToken: String = App.prefs.getItem("accessToken", "no Token")
    private val token = "Bearer ${globalAccessToken.replace("\"", "")}"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWriteFreePostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.freePostUpload.setOnClickListener {
            val title = binding.enteringFreePostTitle.text.toString()
            val body = binding.enteringFreePostBody.text.toString()

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
                    val postingFree = PostingFree(
                        title = title,
                        content = body,
                        image = NULL,
                        postType = "FREE"
                    )
                    GlobalScope.launch(Dispatchers.IO) {
                        try {
                            val responseData = apiService.postingFreePost(token, postingFree)
                            val responseJson = gson.toJson(responseData)
                            val jsonObject = gson.fromJson(responseJson, JsonObject::class.java)
                            val getPostId = jsonObject.get("data").asString
                            withContext(Dispatchers.Main) {
                                val intent = Intent(
                                    this@WriteFreePostActivity,
                                    ViewFreePostActivity::class.java
                                ).apply {
                                    putExtra("fromWriteFreePostId", getPostId.toString())
                                    putExtra("isFromWriteFreeAc", true)
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

        binding.freePostCancel.setOnClickListener {
            val intent = Intent(this, FreePostListActivity::class.java)
            startActivity(intent)
        }
    }
}