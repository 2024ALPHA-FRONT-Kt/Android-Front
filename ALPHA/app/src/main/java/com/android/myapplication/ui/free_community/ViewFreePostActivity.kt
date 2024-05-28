package com.android.myapplication.ui.free_community

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.myapplication.App
import com.android.myapplication.api.RetrofitClient
import com.android.myapplication.databinding.ActivityViewFreePostBinding
import com.google.gson.Gson

class ViewFreePostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewFreePostBinding
    private lateinit var adapter: FreePostAdapter
    private var currentPage = 0

    private val apiService = RetrofitClient.apiservice
    private val gson = Gson()
    private val globalAccessToken: String = App.prefs.getItem("accessToken", "no Token")
    private val token = "Bearer ${globalAccessToken.replace("\"", "")}"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewFreePostBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}