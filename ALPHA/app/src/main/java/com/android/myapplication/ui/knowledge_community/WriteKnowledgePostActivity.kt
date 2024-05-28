package com.android.myapplication.ui.knowledge_community

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.myapplication.App
import com.android.myapplication.api.RetrofitClient
import com.android.myapplication.databinding.ActivityWriteKnowledgePostBinding
import com.android.myapplication.ui.knowledge_community.data_class.PostingKnowledge
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
                        id = "", // todo 여기어
                        title = title,
                        content = body,
                        image = "null",
                        postType = "QNA"
                    )
                    GlobalScope.launch(Dispatchers.IO) {
                        try {
                            val responseData = apiService.postingKnowledgePost(token, postingKnowledge)
                            // todo 업로드 성공
                        } catch (e: Exception) {
                            // todo 업로드 실패
                        }
                    }
                }
            }
        }

        binding.writingKnowledgePostCancel.setOnClickListener {
            // todo 취소 버튼 클릭 시
        }
    }
}
