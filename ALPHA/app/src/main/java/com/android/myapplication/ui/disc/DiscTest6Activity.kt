package com.android.myapplication.ui.disc

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.myapplication.databinding.ActivityDiscTest6Binding
import com.android.myapplication.ui.disc.data_class.DiscScore

class DiscTest6Activity : AppCompatActivity() {

    private lateinit var binding: ActivityDiscTest6Binding
    private lateinit var discScore: DiscScore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiscTest6Binding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        discScore = intent.getParcelableExtra("DISC_SCORE") ?: DiscScore()

        setEditTextInputType()

        binding.discNextPage6.setOnClickListener {
            if (validateInputs()) {
                val intent = Intent(this, DiscTest7Activity::class.java).apply {
                    putExtra("DISC_SCORE", discScore)
                }
                startActivity(intent)
            }
        }

        binding.discTestBackButton6.setOnClickListener {
            binding.discQ11A1.text.clear()
            binding.discQ11A2.text.clear()
            binding.discQ11A3.text.clear()
            binding.discQ11A4.text.clear()
            binding.discQ12A1.text.clear()
            binding.discQ12A2.text.clear()
            binding.discQ12A3.text.clear()
            binding.discQ12A4.text.clear()
            binding.discQ13A1.text.clear()
            binding.discQ13A2.text.clear()
            binding.discQ13A3.text.clear()
            binding.discQ13A4.text.clear()
            val intent = Intent(this, DiscTest5Activity::class.java).apply {
                putExtra("DISC_SCORE", discScore)
            }
            startActivity(intent)
        }
    }

    private fun setEditTextInputType() {
        val editTexts = listOf(
            binding.discQ11A1, binding.discQ11A2, binding.discQ11A3, binding.discQ11A4,
            binding.discQ12A1, binding.discQ12A2, binding.discQ12A3, binding.discQ12A4,
            binding.discQ13A1, binding.discQ13A2, binding.discQ13A3, binding.discQ13A4
        )

        for (editText in editTexts) {
            editText.inputType = InputType.TYPE_CLASS_NUMBER
            editText.filters = arrayOf(InputFilter.LengthFilter(1))
        }
    }

    private fun validateInputs(): Boolean {
        val q11Values = mutableSetOf<Int>()
        val q12Values = mutableSetOf<Int>()
        val q13Values = mutableSetOf<Int>()

        for (i in 1..4) {
            q11Values.add(i)
            q12Values.add(i)
            q13Values.add(i)
        }

        val editTexts = listOf(
            binding.discQ11A1, binding.discQ11A2, binding.discQ11A3, binding.discQ11A4,
            binding.discQ12A1, binding.discQ12A2, binding.discQ12A3, binding.discQ12A4,
            binding.discQ13A1, binding.discQ13A2, binding.discQ13A3, binding.discQ13A4
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
                binding.discQ11A1, binding.discQ12A1, binding.discQ13A1 -> discScore.DScore += value
                binding.discQ11A2, binding.discQ12A2, binding.discQ13A2 -> discScore.IScore += value
                binding.discQ11A3, binding.discQ12A3, binding.discQ13A3 -> discScore.SScore += value
                binding.discQ11A4, binding.discQ12A4, binding.discQ13A4 -> discScore.CScore += value
            }

            if (editText in listOf(
                    binding.discQ11A1,
                    binding.discQ11A2,
                    binding.discQ11A3,
                    binding.discQ11A4
                )
            ) {
                if (!q11Values.remove(value)) {
                    Toast.makeText(this, "각 문항 당 1~4 범위 내 숫자를 한 번씩만 입력해 주세요!", Toast.LENGTH_LONG)
                        .show()
                    return false
                }
            } else if (editText in listOf(
                    binding.discQ12A1,
                    binding.discQ12A2,
                    binding.discQ12A3,
                    binding.discQ12A4
                )
            ) {
                if (!q12Values.remove(value)) {
                    Toast.makeText(this, "각 문항 당 1~4 범위 내 숫자를 한 번씩만 입력해 주세요!", Toast.LENGTH_LONG)
                        .show()
                    return false
                }
            } else if (editText in listOf(
                    binding.discQ13A1,
                    binding.discQ13A2,
                    binding.discQ13A3,
                    binding.discQ13A4
                )
            ) {
                if (!q13Values.remove(value)) {
                    Toast.makeText(this, "각 문항 당 1~4 범위 내 숫자를 한 번씩만 입력해 주세요!", Toast.LENGTH_LONG)
                        .show()
                    return false
                }
            }
        }

        return true
    }
}