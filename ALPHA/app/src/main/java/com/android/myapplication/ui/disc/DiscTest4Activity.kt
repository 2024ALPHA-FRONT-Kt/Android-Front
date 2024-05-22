package com.android.myapplication.ui.disc

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.myapplication.databinding.ActivityDiscTest4Binding

class DiscTest4Activity : AppCompatActivity() {

    private lateinit var binding: ActivityDiscTest4Binding
    private lateinit var discScore: DiscScore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiscTest4Binding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        discScore = intent.getParcelableExtra("DISC_SCORE") ?: DiscScore()

        setEditTextInputType()

        binding.discNextPage4.setOnClickListener {
            if (validateInputs()) {
                val intent = Intent(this, DiscTest5Activity::class.java).apply {
                    putExtra("DISC_SCORE", discScore)
                }
                startActivity(intent)
            }
        }

        binding.discTestBackButton4.setOnClickListener {
            binding.discQ7A1.text.clear()
            binding.discQ7A2.text.clear()
            binding.discQ7A3.text.clear()
            binding.discQ7A4.text.clear()
            binding.discQ8A1.text.clear()
            binding.discQ8A2.text.clear()
            binding.discQ8A3.text.clear()
            binding.discQ8A4.text.clear()
            val intent = Intent(this, DiscTest3Activity::class.java).apply {
                putExtra("DISC_SCORE", discScore)
            }
            startActivity(intent)
        }
    }

    private fun setEditTextInputType() {
        val editTexts = listOf(
            binding.discQ7A1, binding.discQ7A2, binding.discQ7A3, binding.discQ7A4,
            binding.discQ8A1, binding.discQ8A2, binding.discQ8A3, binding.discQ8A4
        )

        for (editText in editTexts) {
            editText.inputType = InputType.TYPE_CLASS_NUMBER
            editText.filters = arrayOf(InputFilter.LengthFilter(1))
        }
    }

    private fun validateInputs(): Boolean {
        val q7Values = mutableSetOf<Int>()
        val q8Values = mutableSetOf<Int>()

        for (i in 1..4) {
            q7Values.add(i)
            q8Values.add(i)
        }

        val editTexts = listOf(
            binding.discQ7A1, binding.discQ7A2, binding.discQ7A3, binding.discQ7A4,
            binding.discQ8A1, binding.discQ8A2, binding.discQ8A3, binding.discQ8A4
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
                binding.discQ7A1, binding.discQ8A1 -> discScore.DScore += value
                binding.discQ7A2, binding.discQ8A2 -> discScore.IScore += value
                binding.discQ7A3, binding.discQ8A3 -> discScore.SScore += value
                binding.discQ7A4, binding.discQ8A4 -> discScore.CScore += value
            }

            if (editText in listOf(binding.discQ7A1, binding.discQ7A2, binding.discQ7A3, binding.discQ7A4)) {
                if (!q7Values.remove(value)) {
                    Toast.makeText(this, "각 문항 당 1~4 범위 내 숫자를 한 번씩만 입력해 주세요!", Toast.LENGTH_LONG).show()
                    return false
                }
            } else if (editText in listOf(binding.discQ8A1, binding.discQ8A2, binding.discQ8A3, binding.discQ8A4)) {
                if (!q8Values.remove(value)) {
                    Toast.makeText(this, "각 문항 당 1~4 범위 내 숫자를 한 번씩만 입력해 주세요!", Toast.LENGTH_LONG).show()
                    return false
                }
            }
        }

        return true
    }
}

