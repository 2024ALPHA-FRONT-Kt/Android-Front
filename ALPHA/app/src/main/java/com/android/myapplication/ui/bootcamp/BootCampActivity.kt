package com.android.myapplication.ui.bootcamp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.myapplication.R
import com.android.myapplication.databinding.ActivityBootCampBinding

class BootCampActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBootCampBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boot_camp)
        supportActionBar?.hide()

        // 바인딩
        binding = ActivityBootCampBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}