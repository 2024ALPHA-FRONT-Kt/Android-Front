package com.android.myapplication.ui.disc

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.myapplication.App
import com.android.myapplication.MainActivity
import com.android.myapplication.api.RetrofitClient
import com.android.myapplication.databinding.ActivityDiscBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DiscActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDiscBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        val apiService = RetrofitClient.apiservice
        binding = ActivityDiscBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val globalAccessToken: String = App.prefs.getItem("accessToken", "no Token")
        val token = "Bearer ${globalAccessToken.replace("\"", "")}"

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val responseData = apiService.getDiscUsers(token)
                val discUsers = responseData.data // 이미 int라서 바꿀 수가 없는 것 같은데 우째야 합미까
                withContext(Dispatchers.Main) {
                    binding.discUsers.text = discUsers.toString().substring(0 until -2)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        binding.discTestStartButton.setOnClickListener {
            startActivity(Intent(this, DiscTestActivity::class.java))
        }

        binding.discStartPageBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}