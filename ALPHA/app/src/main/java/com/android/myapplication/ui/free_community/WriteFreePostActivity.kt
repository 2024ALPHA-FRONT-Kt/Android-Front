package com.android.myapplication.ui.free_community

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.myapplication.App
import com.android.myapplication.api.RetrofitClient
import com.android.myapplication.databinding.ActivityWriteFreePostBinding
import com.google.gson.Gson

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


    }
}