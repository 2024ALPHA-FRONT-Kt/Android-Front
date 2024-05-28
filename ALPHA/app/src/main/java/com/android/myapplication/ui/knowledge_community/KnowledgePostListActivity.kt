package com.android.myapplication.ui.knowledge_community

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.myapplication.App
import com.android.myapplication.api.RetrofitClient
import com.android.myapplication.databinding.ActivityKnowledgePostListBinding
import com.google.gson.Gson

class KnowledgePostListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKnowledgePostListBinding
    private val apiService = RetrofitClient.apiservice
    private val gson = Gson()
    private val globalAccessToken: String = App.prefs.getItem("accessToken", "no Token")
    private val token = "Bearer ${globalAccessToken.replace("\"", "")}"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKnowledgePostListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val list = ArrayList<KnowledgePosts>()
        list.add(
            KnowledgePosts(
                "저 학교 어떻게 다녀야 할까요?",
                "저 생기부 작성하려고 하는데 혹시 스포츠 건강재활학광서 어떤 것ㅇㄹ 강조하는지 혹시 꿀팁 주실 수 있나요 ㅠㅠ 주면 감사랑 치킨",
                "0000-00-00"
            )
        )
        list.add(
            KnowledgePosts(
                "저 학교 어떻게 다녀야 할까요?",
                "저 생기부 작성하려고 하는데 혹시 스포츠 건강재활학광서 어떤 것ㅇㄹ 강조하는지 혹시 꿀팁 주실 수 있나요 ㅠㅠ 주면 감사랑 치킨",
                "0000-00-00"
            )
        )
        list.add(
            KnowledgePosts(
                "저 학교 어떻게 다녀야 할까요?",
                "저 생기부 작성하려고 하는데 혹시 스포츠 건강재활학광서 어떤 것ㅇㄹ 강조하는지 혹시 꿀팁 주실 수 있나요 ㅠㅠ 주면 감사랑 치킨",
                "0000-00-00"
            )
        )
        list.add(
            KnowledgePosts(
                "저 학교 어떻게 다녀야 할까요?",
                "저 생기부 작성하려고 하는데 혹시 스포츠 건강재활학광서 어떤 것ㅇㄹ 강조하는지 혹시 꿀팁 주실 수 있나요 ㅠㅠ 주면 감사랑 치킨",
                "0000-00-00"
            )
        )
        list.add(
            KnowledgePosts(
                "저 학교 어떻게 다녀야 할까요?",
                "저 생기부 작성하려고 하는데 혹시 스포츠 건강재활학광서 어떤 것ㅇㄹ 강조하는지 혹시 꿀팁 주실 수 있나요 ㅠㅠ 주면 감사랑 치킨",
                "0000-00-00"
            )
        )

        val recyclerView = binding.knowledgeListView
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = KnowledgePostsAdapter(list) {
            // todo
        }

        binding.knowledgePostsListWritingButton.setOnClickListener {
            val intent = Intent(this, WriteKnowledgePostActivity::class.java)
            startActivity(intent)
        }

        recyclerView.adapter = adapter
    }
}