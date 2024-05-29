package com.android.myapplication.ui.bootcamp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.myapplication.R
import com.android.myapplication.databinding.ActivityCampClassBinding
import com.android.myapplication.databinding.ActivityCampSelectBinding

class CampSelectActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCampSelectBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camp_select)
        supportActionBar?.hide()

        // 바인딩
        binding = ActivityCampSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.item1Btn3.setOnClickListener {
            if (binding.item1Btn3.text == "신청하기"){
                binding.item1Btn1.visibility = View.VISIBLE
                binding.item1Btn1.text = "상세보기"
                binding.item1Btn2.text = "강의입장"
                binding.item1Btn3.text = "취소하기"
            } else {
                binding.item1Btn1.visibility = View.INVISIBLE
                binding.item1Btn2.text = "상세보기"
                binding.item1Btn3.text = "신청하기"
            }
        }

        binding.item1Btn2.setOnClickListener {
            if (binding.item1Btn2.text == "상세보기"){
                // 상세보기 1
                val intent = Intent(this, CampInfo1Activity::class.java)
                startActivity(intent)
            } else {
                // 강의입장 1
                val intent = Intent(this, CampClassActivity::class.java)
                startActivity(intent)
            }
        }

        binding.item1Btn1.setOnClickListener {
            if (binding.item1Btn1.visibility == View.VISIBLE) {
                // 상세보기 1
                val intent = Intent(this, CampInfo1Activity::class.java)
                startActivity(intent)
            }
        }

        binding.item2Btn2.setOnClickListener {
            // 상세보기 2
            val intent = Intent(this, CampInfo2Activity::class.java)
            startActivity(intent)
        }

    }
}