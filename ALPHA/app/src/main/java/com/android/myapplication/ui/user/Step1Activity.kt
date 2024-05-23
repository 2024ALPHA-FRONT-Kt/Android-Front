package com.android.myapplication.ui.user

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.myapplication.MainActivity
import com.android.myapplication.R
import com.android.myapplication.databinding.ActivityLogInBinding
import com.android.myapplication.databinding.ActivityStep1Binding
import com.android.myapplication.databinding.FragmentHomeBinding

class Step1Activity : AppCompatActivity() {
    private lateinit var binding: ActivityStep1Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step1)
        supportActionBar?.hide()

        // 숫자로 구분
        var univ = 0

        binding.high.setOnClickListener{
            binding.high.setImageResource(R.drawable.img_sign_in_step1_high)
        }
        binding.univ.setOnClickListener{
            binding.high.setImageResource(R.drawable.img_sign_in_step1_univ)
            univ = 1
        }

        if (univ == 1){
            val intent = Intent(this, Step2Univ1Activity::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(this, Step2HighActivity::class.java)
            startActivity(intent)
        }

    }
}