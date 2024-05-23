package com.android.myapplication.ui.user

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.myapplication.R
import com.android.myapplication.databinding.ActivityEditBinding
import com.android.myapplication.databinding.ActivityStep2Univ1Binding

class Step2Univ1Activity : AppCompatActivity() {
    private lateinit var binding: ActivityStep2Univ1Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step2_univ1)
        supportActionBar?.hide()

        // 바인딩
        binding = ActivityStep2Univ1Binding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}