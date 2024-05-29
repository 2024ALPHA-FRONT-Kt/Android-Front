package com.android.myapplication.ui.disc

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.myapplication.databinding.ActivityDiscTest2Binding
import com.android.myapplication.ui.disc.data_class.DiscScore

class DiscTest2Activity : AppCompatActivity() {

    private lateinit var binding: ActivityDiscTest2Binding
    private lateinit var discScore: DiscScore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiscTest2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        discScore = intent.getParcelableExtra("DISC_SCORE") ?: DiscScore()

        setEditTextInputType()

        binding.discNextPage2.setOnClickListener {
            if (validateInputs()) {
                val intent = Intent(this, DiscTest3Activity::class.java).apply {
                    putExtra("DISC_SCORE", discScore)
                }
                startActivity(intent)
            }
        }

        binding.discTestBackButton2.setOnClickListener {
            binding.discQ3A1.text.clear()
            binding.discQ3A2.text.clear()
            binding.discQ3A3.text.clear()
            binding.discQ3A4.text.clear()
            binding.discQ4A1.text.clear()
            binding.discQ4A2.text.clear()
            binding.discQ4A3.text.clear()
            binding.discQ4A4.text.clear()
            val intent = Intent(this, DiscTestActivity::class.java).apply {
                putExtra("DISC_SCORE", discScore)
            }
            startActivity(intent)
        }
    }

    private fun setEditTextInputType() {
        val editTexts = listOf(
            binding.discQ3A1, binding.discQ3A2, binding.discQ3A3, binding.discQ3A4,
            binding.discQ4A1, binding.discQ4A2, binding.discQ4A3, binding.discQ4A4
        )

        for (editText in editTexts) {
            editText.inputType = InputType.TYPE_CLASS_NUMBER
            editText.filters = arrayOf(InputFilter.LengthFilter(1))
        }
    }

    private fun validateInputs(): Boolean {
        val q3Values = mutableSetOf<Int>()
        val q4Values = mutableSetOf<Int>()

        for (i in 1..4) {
            q3Values.add(i)
            q4Values.add(i)
        }

        val editTexts = listOf(
            binding.discQ3A1, binding.discQ3A2, binding.discQ3A3, binding.discQ3A4,
            binding.discQ4A1, binding.discQ4A2, binding.discQ4A3, binding.discQ4A4
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
                binding.discQ3A1, binding.discQ4A1 -> discScore.DScore += value
                binding.discQ3A2, binding.discQ4A2 -> discScore.IScore += value
                binding.discQ3A3, binding.discQ4A3 -> discScore.SScore += value
                binding.discQ3A4, binding.discQ4A4 -> discScore.CScore += value
            }

            if (editText in listOf(binding.discQ3A1, binding.discQ3A2, binding.discQ3A3, binding.discQ3A4)) {
                if (!q3Values.remove(value)) {
                    Toast.makeText(this, "각 문항 당 1~4 범위 내 숫자를 한 번씩만 입력해 주세요!", Toast.LENGTH_LONG).show()
                    return false
                }
            } else if (editText in listOf(binding.discQ4A1, binding.discQ4A2, binding.discQ4A3, binding.discQ4A4)) {
                if (!q4Values.remove(value)) {
                    Toast.makeText(this, "각 문항 당 1~4 범위 내 숫자를 한 번씩만 입력해 주세요!", Toast.LENGTH_LONG).show()
                    return false
                }
            }
        }

        return true
    }
}