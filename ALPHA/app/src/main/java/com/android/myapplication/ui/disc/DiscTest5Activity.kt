package com.android.myapplication.ui.disc

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.myapplication.databinding.ActivityDiscTest5Binding

class DiscTest5Activity : AppCompatActivity() {

    private lateinit var binding: ActivityDiscTest5Binding
    private lateinit var discScore: DiscScore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiscTest5Binding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        discScore = intent.getParcelableExtra("DISC_SCORE") ?: DiscScore()

        setEditTextInputType()

        binding.discNextPage5.setOnClickListener {
            if (validateInputs()) {
                val intent = Intent(this, DiscTest6Activity::class.java).apply {
                    putExtra("DISC_SCORE", discScore)
                }
                startActivity(intent)
            }
        }

        binding.discTestBackButton5.setOnClickListener {
            binding.discQ9A1.text.clear()
            binding.discQ9A2.text.clear()
            binding.discQ9A3.text.clear()
            binding.discQ9A4.text.clear()
            binding.discQ10A1.text.clear()
            binding.discQ10A2.text.clear()
            binding.discQ10A3.text.clear()
            binding.discQ10A4.text.clear()
            val intent = Intent(this, DiscTest4Activity::class.java).apply {
                putExtra("DISC_SCORE", discScore)
            }
            startActivity(intent)
        }

        val progressBar = binding.discProgressBar
        progressBar.updateDiscProBar(10)
    }

    private fun setEditTextInputType() {
        val editTexts = listOf(
            binding.discQ9A1, binding.discQ9A2, binding.discQ9A3, binding.discQ9A4,
            binding.discQ10A1, binding.discQ10A2, binding.discQ10A3, binding.discQ10A4
        )

        for (editText in editTexts) {
            editText.inputType = InputType.TYPE_CLASS_NUMBER
            editText.filters = arrayOf(InputFilter.LengthFilter(1))
        }
    }

    private fun validateInputs(): Boolean {
        val q9Values = mutableSetOf<Int>()
        val q10Values = mutableSetOf<Int>()

        for (i in 1..4) {
            q9Values.add(i)
            q10Values.add(i)
        }

        val editTexts = listOf(
            binding.discQ9A1, binding.discQ9A2, binding.discQ9A3, binding.discQ9A4,
            binding.discQ10A1, binding.discQ10A2, binding.discQ10A3, binding.discQ10A4
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
                binding.discQ9A1, binding.discQ10A1 -> discScore.DScore += value
                binding.discQ9A2, binding.discQ10A2 -> discScore.IScore += value
                binding.discQ9A3, binding.discQ10A3 -> discScore.SScore += value
                binding.discQ9A4, binding.discQ10A4 -> discScore.CScore += value
            }

            if (editText in listOf(binding.discQ9A1, binding.discQ9A2, binding.discQ9A3, binding.discQ9A4)) {
                if (!q9Values.remove(value)) {
                    Toast.makeText(this, "각 문항 당 1~4 범위 내 숫자를 한 번씩만 입력해 주세요!", Toast.LENGTH_LONG).show()
                    return false
                }
            } else if (editText in listOf(binding.discQ10A1, binding.discQ10A2, binding.discQ10A3, binding.discQ10A4)) {
                if (!q10Values.remove(value)) {
                    Toast.makeText(this, "각 문항 당 1~4 범위 내 숫자를 한 번씩만 입력해 주세요!", Toast.LENGTH_LONG).show()
                    return false
                }
            }
        }

        return true
    }
}
