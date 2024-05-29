package com.android.myapplication.ui.bootcamp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.myapplication.R
import com.android.myapplication.databinding.ActivityCampBinding

class CampActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCampBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camp)
        supportActionBar?.hide()

        // 바인딩
        binding = ActivityCampBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bootCampStartBack.setOnClickListener {
            onBackPressed()
        }

        binding.bootCampStartButton.setOnClickListener{
            val intent = Intent(this, CampSelectActivity::class.java)
            startActivity(intent)
        }

        binding.bootCampStartBack.setOnClickListener {
            onBackPressed()
        }
    }
}