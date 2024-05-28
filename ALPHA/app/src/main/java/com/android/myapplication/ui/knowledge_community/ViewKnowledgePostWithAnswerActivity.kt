package com.android.myapplication.ui.knowledge_community

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.myapplication.App
import com.android.myapplication.api.RetrofitClient
import com.android.myapplication.databinding.ActivityViewKnowledgePostWithAnswerBinding
import com.google.gson.Gson

class ViewKnowledgePostWithAnswerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewKnowledgePostWithAnswerBinding
    private val apiService = RetrofitClient.apiservice
    private val gson = Gson()
    private val globalAccessToken: String = App.prefs.getItem("accessToken", "no Token")
    private val token = "Bearer ${globalAccessToken.replace("\"", "")}"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewKnowledgePostWithAnswerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
    }
}