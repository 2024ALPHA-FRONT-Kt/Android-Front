package com.android.myapplication.ui.disc

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.myapplication.R
import com.android.myapplication.databinding.ActivityDiscTestBinding

class DiscTestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDiscTestBinding
    private var discScore = DiscScore()

    // 진행 상태를 저장하는 변수
    private var progressValue: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiscTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setEditTextInputType()

        binding.discNextPage.setOnClickListener {
            if (validateInputs()) {
                val intent = Intent(this, DiscTest2Activity::class.java).apply {
                    putExtra("DISC_SCORE", discScore)
                }
                startActivity(intent)
            }
        }

        binding.discTestBackButton1.setOnClickListener {
            binding.discQ1A1.text.clear()
            binding.discQ1A2.text.clear()
            binding.discQ1A3.text.clear()
            binding.discQ1A4.text.clear()
            binding.discQ2A1.text.clear()
            binding.discQ2A2.text.clear()
            binding.discQ2A3.text.clear()
            binding.discQ2A4.text.clear()
            val intent = Intent(this, DiscActivity::class.java).apply {
                putExtra("DISC_SCORE", discScore)
            }
            startActivity(intent)
        }

        val progressBar = binding.discProgressBar
        progressBar.updateDiscProBar(10)
    }

    private fun setEditTextInputType() {
        val editTexts = listOf(
            binding.discQ1A1, binding.discQ1A2, binding.discQ1A3, binding.discQ1A4,
            binding.discQ2A1, binding.discQ2A2, binding.discQ2A3, binding.discQ2A4
        )

        for (editText in editTexts) {
            editText.inputType = InputType.TYPE_CLASS_NUMBER
            editText.filters = arrayOf(InputFilter.LengthFilter(1))
        }
    }

    private fun validateInputs(): Boolean {
        val q1Values = mutableSetOf<Int>()
        val q2Values = mutableSetOf<Int>()

        for (i in 1..4) {
            q1Values.add(i)
            q2Values.add(i)
        }

        val editTexts = listOf(
            binding.discQ1A1, binding.discQ1A2, binding.discQ1A3, binding.discQ1A4,
            binding.discQ2A1, binding.discQ2A2, binding.discQ2A3, binding.discQ2A4
        )

        for (editText in editTexts) {
            val text = editText.text.toString()

            if (text.isEmpty()) {
                Toast.makeText(this, "모든 문항에 숫자를 입력해 주세요!", Toast.LENGTH_LONG).show()
                return false
            }

            val value = text.toIntOrNull()
            if (value == null || value !in 1..4) {
                Toast.makeText(this, "1~4 범위 내의 숫자를 입력해 주세요!", Toast.LENGTH_LONG).show()
                return false
            }

            when (editText) {
                binding.discQ1A1, binding.discQ2A1 -> discScore.DScore += value
                binding.discQ1A2, binding.discQ2A2 -> discScore.IScore += value
                binding.discQ1A3, binding.discQ2A3 -> discScore.SScore += value
                binding.discQ1A4, binding.discQ2A4 -> discScore.CScore += value
            }

            if (editText in listOf(binding.discQ1A1, binding.discQ1A2, binding.discQ1A3, binding.discQ1A4)) {
                if (!q1Values.remove(value)) {
                    Toast.makeText(this, "각 문항 당 1~4 범위 내 숫자를 한 번씩만 입력해 주세요!", Toast.LENGTH_LONG).show()
                    return false
                }
            } else if (editText in listOf(binding.discQ2A1, binding.discQ2A2, binding.discQ2A3, binding.discQ2A4)) {
                if (!q2Values.remove(value)) {
                    Toast.makeText(this, "각 문항 당 1~4 범위 내 숫자를 한 번씩만 입력해 주세요!", Toast.LENGTH_LONG).show()
                    return false
                }
            }
        }

        return true
    }
}

fun ProgressBar.updateDiscProBar(step: Int) {
    progress += step
}