package com.android.myapplication.ui.bootcamp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.myapplication.R
import com.android.myapplication.databinding.ActivityCampClassBinding

class CampClassActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCampClassBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camp_class)
        supportActionBar?.hide()
        // 바인딩
        binding = ActivityCampClassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.link.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("${binding.link.text}"))
            startActivity(intent)
        }

        binding.campClassBack.setOnClickListener {
            onBackPressed()
        }
    }
}