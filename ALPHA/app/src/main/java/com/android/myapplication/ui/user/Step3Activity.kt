package com.android.myapplication.ui.user

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.myapplication.R
import com.android.myapplication.databinding.ActivityStep3Binding

class Step3Activity : AppCompatActivity() {
    private lateinit var binding: ActivityStep3Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step3)
        supportActionBar?.hide()

        // 바인딩
        binding = ActivityStep3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStep3EditSave.setOnClickListener {
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
        }

    }
}