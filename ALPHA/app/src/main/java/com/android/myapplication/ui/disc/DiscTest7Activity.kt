package com.android.myapplication.ui.disc

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.myapplication.databinding.ActivityDiscTest7Binding
import com.android.myapplication.ui.disc.data_class.DiscScore

class DiscTest7Activity : AppCompatActivity() {

    private lateinit var binding: ActivityDiscTest7Binding
    private lateinit var discScore: DiscScore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiscTest7Binding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        discScore = intent.getParcelableExtra("DISC_SCORE") ?: DiscScore()

        setEditTextInputType()

        binding.discNextPage7.setOnClickListener {
            if (validateInputs()) {
                Log.d("DiscTest7Activity", "Passing DISC_SCORE: $discScore") // 로그 추가
                val intent = Intent(this, DiscResultActivity::class.java).apply {
                    putExtra("DISC_SCORE", discScore)
                }
                startActivity(intent)
            }
        }

        binding.discTestBackButton7.setOnClickListener {
            binding.discQ14A1.text.clear()
            binding.discQ14A2.text.clear()
            binding.discQ14A3.text.clear()
            binding.discQ14A4.text.clear()
            binding.discQ15A1.text.clear()
            binding.discQ15A2.text.clear()
            binding.discQ15A3.text.clear()
            binding.discQ15A4.text.clear()
            val intent = Intent(this, DiscTest6Activity::class.java).apply {
                putExtra("DISC_SCORE", discScore)
            }
            startActivity(intent)
        }
    }

    private fun setEditTextInputType() {
        val editTexts = listOf(
            binding.discQ14A1, binding.discQ14A2, binding.discQ14A3, binding.discQ14A4,
            binding.discQ15A1, binding.discQ15A2, binding.discQ15A3, binding.discQ15A4
        )

        for (editText in editTexts) {
            editText.inputType = InputType.TYPE_CLASS_NUMBER
            editText.filters = arrayOf(InputFilter.LengthFilter(1))
        }
    }

    private fun validateInputs(): Boolean {
        val q14Values = mutableSetOf<Int>()
        val q15Values = mutableSetOf<Int>()

        for (i in 1..4) {
            q14Values.add(i)
            q15Values.add(i)
        }

        val editTexts = listOf(
            binding.discQ14A1, binding.discQ14A2, binding.discQ14A3, binding.discQ14A4,
            binding.discQ15A1, binding.discQ15A2, binding.discQ15A3, binding.discQ15A4
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
                binding.discQ14A1 -> discScore.DScore += value
                binding.discQ14A2 -> discScore.IScore += value
                binding.discQ14A3 -> discScore.SScore += value
                binding.discQ14A4 -> discScore.CScore += value
                binding.discQ15A1 -> discScore.DScore += value
                binding.discQ15A2 -> discScore.IScore += value
                binding.discQ15A3 -> discScore.SScore += value
                binding.discQ15A4 -> discScore.CScore += value
            }

            if (editText in listOf(
                    binding.discQ14A1,
                    binding.discQ14A2,
                    binding.discQ14A3,
                    binding.discQ14A4
                )
            ) {
                if (!q14Values.remove(value)) {
                    Toast.makeText(this, "각 문항 당 1~4 범위 내 숫자를 한 번씩만 입력해 주세요!", Toast.LENGTH_LONG)
                        .show()
                    return false
                }
            } else if (editText in listOf(
                    binding.discQ15A1,
                    binding.discQ15A2,
                    binding.discQ15A3,
                    binding.discQ15A4
                )
            ) {
                if (!q15Values.remove(value)) {
                    Toast.makeText(this, "각 문항 당 1~4 범위 내 숫자를 한 번씩만 입력해 주세요!", Toast.LENGTH_LONG)
                        .show()
                    return false
                }
            }
        }

        return true
    }
}