package com.android.myapplication.ui.disc

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.myapplication.databinding.ActivityDiscTest3Binding

class DiscTest3Activity : AppCompatActivity() {

    private lateinit var binding: ActivityDiscTest3Binding
    private lateinit var discScore: DiscScore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiscTest3Binding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        discScore = intent.getParcelableExtra("DISC_SCORE") ?: DiscScore()

        setEditTextInputType()

        binding.discNextPage3.setOnClickListener {
            if (validateInputs()) {
                val intent = Intent(this, DiscTest4Activity::class.java).apply {
                    putExtra("DISC_SCORE", discScore)
                }
                startActivity(intent)
            }
        }

        binding.discTestBackButton3.setOnClickListener {
            binding.discQ5A1.text.clear()
            binding.discQ5A2.text.clear()
            binding.discQ5A3.text.clear()
            binding.discQ5A4.text.clear()
            binding.discQ6A1.text.clear()
            binding.discQ6A2.text.clear()
            binding.discQ6A3.text.clear()
            binding.discQ6A4.text.clear()
            val intent = Intent(this, DiscTest2Activity::class.java).apply {
                putExtra("DISC_SCORE", discScore)
            }
            startActivity(intent)
        }
    }

    private fun setEditTextInputType() {
        val editTexts = listOf(
            binding.discQ5A1, binding.discQ5A2, binding.discQ5A3, binding.discQ5A4,
            binding.discQ6A1, binding.discQ6A2, binding.discQ6A3, binding.discQ6A4
        )

        for (editText in editTexts) {
            editText.inputType = InputType.TYPE_CLASS_NUMBER
            editText.filters = arrayOf(InputFilter.LengthFilter(1))
        }
    }

    private fun validateInputs(): Boolean {
        val q5Values = mutableSetOf<Int>()
        val q6Values = mutableSetOf<Int>()

        for (i in 1..4) {
            q5Values.add(i)
            q6Values.add(i)
        }

        val editTexts = listOf(
            binding.discQ5A1, binding.discQ5A2, binding.discQ5A3, binding.discQ5A4,
            binding.discQ6A1, binding.discQ6A2, binding.discQ6A3, binding.discQ6A4
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
                binding.discQ5A1, binding.discQ6A1 -> discScore.DScore += value
                binding.discQ5A2, binding.discQ6A2 -> discScore.IScore += value
                binding.discQ5A3, binding.discQ6A3 -> discScore.SScore += value
                binding.discQ5A4, binding.discQ6A4 -> discScore.CScore += value
            }

            if (editText in listOf(binding.discQ5A1, binding.discQ5A2, binding.discQ5A3, binding.discQ5A4)) {
                if (!q5Values.remove(value)) {
                    Toast.makeText(this, "각 문항 당 1~4 범위 내 숫자를 한 번씩만 입력해 주세요!", Toast.LENGTH_LONG).show()
                    return false
                }
            } else if (editText in listOf(binding.discQ6A1, binding.discQ6A2, binding.discQ6A3, binding.discQ6A4)) {
                if (!q6Values.remove(value)) {
                    Toast.makeText(this, "각 문항 당 1~4 범위 내 숫자를 한 번씩만 입력해 주세요!", Toast.LENGTH_LONG).show()
                    return false
                }
            }
        }

        return true
    }
}