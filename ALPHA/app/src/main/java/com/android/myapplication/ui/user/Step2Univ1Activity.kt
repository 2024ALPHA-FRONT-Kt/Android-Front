package com.android.myapplication.ui.user

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.myapplication.MainActivity
import com.android.myapplication.R
import com.android.myapplication.databinding.ActivityEditBinding
import com.android.myapplication.databinding.ActivityStep2Univ1Binding

class Step2Univ1Activity : AppCompatActivity() {
    private lateinit var binding: ActivityStep2Univ1Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step2_univ1)
        supportActionBar?.hide()
        var newUniv = ""
        var newEmail = ""

        // 바인딩
        binding = ActivityStep2Univ1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGetPasskey.setOnClickListener {
            newUniv = binding.newUnivU.toString()
            newEmail = binding.newEmail.toString()
        }

        var valid = 0 // 인증결과
        binding.btnCheckPasskey.setOnClickListener {
            // 인증잘되면
            valid = 1
            binding.newUnivU.isEnabled = false
            binding.univEmail.isEnabled = false
            Toast.makeText(applicationContext,"인증이 완료되었습니다.", Toast.LENGTH_SHORT).show()
            // 안되면
            Toast.makeText(applicationContext,"이메일 인증을 다시 해주세요", Toast.LENGTH_SHORT).show()
        }

        binding.btnNext.setOnClickListener {
            if (valid == 1){
                val intent = Intent(this, Step2Univ2Activity::class.java)
                intent.putExtra("univ",newUniv)
                intent.putExtra("email",newEmail)
                startActivity(intent)
            } else {
                Toast.makeText(applicationContext,"이메일 인증이 완료되지 않았습니다.", Toast.LENGTH_SHORT).show()
            }
        }



    }
}