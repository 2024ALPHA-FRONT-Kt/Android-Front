package com.android.myapplication.ui.user

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.myapplication.R
import com.android.myapplication.databinding.ActivityStep2HighBinding

class Step2HighActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStep2HighBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step2_high)
        supportActionBar?.hide()

        val newName = binding.newName
        val newAge = binding.newAge
        val newMan = binding.newMan
        val newWoman = binding.newWoman
        val newId = binding.newId
        val newPw = binding.newPw
        val newPwRe = binding.newPwRe
        val newUnivH = binding.newUnivH
        val newDepartH = binding.newDepartH

        // 성별 확인
        // 아이디 중복 확인
        // 비밀번호 일치 확인
    }
}