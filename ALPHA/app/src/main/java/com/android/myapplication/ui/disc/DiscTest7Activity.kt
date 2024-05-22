package com.android.myapplication.ui.disc

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.myapplication.R

class DiscTest7Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_disc_test7)
        supportActionBar?.hide()

        setEditTextInputType()

        findViewById<Button>(R.id.disc_next_page_7).setOnClickListener {
            if (validateInputs()) {
                val intent = Intent(this, DiscResultActivity::class.java)
                startActivity(intent)
            }
        }

        findViewById<ImageView>(R.id.disc_test_back_button_7).setOnClickListener {
            val intent = Intent(this, DiscTest6Activity::class.java)
            startActivity(intent)
        }

        val progressBar = findViewById<ProgressBar>(R.id.disc_progress_bar)
        progressBar.updateDiscProBar(10)
    }

    private fun setEditTextInputType() {
        val editTexts = listOf(
            R.id.disc_q14_a1, R.id.disc_q14_a2, R.id.disc_q14_a3, R.id.disc_q14_a4,
            R.id.disc_q15_a1, R.id.disc_q15_a2, R.id.disc_q15_a3, R.id.disc_q15_a4
        )

        for (editTextId in editTexts) {
            val editText = findViewById<EditText>(editTextId)
            editText.inputType = InputType.TYPE_CLASS_NUMBER
            editText.filters = arrayOf(InputFilter.LengthFilter(1))
        }
    }

    private fun validateInputs(): Boolean {
        val q14Values = mutableSetOf(1, 2, 3, 4)
        val q15Values = mutableSetOf(1, 2, 3, 4)

        val editTexts = listOf(
            R.id.disc_q14_a1, R.id.disc_q14_a2, R.id.disc_q14_a3, R.id.disc_q14_a4,
            R.id.disc_q15_a1, R.id.disc_q15_a2, R.id.disc_q15_a3, R.id.disc_q15_a4
        )

        for (editTextId in editTexts) {
            val editText = findViewById<EditText>(editTextId)
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

            when (editTextId) {
                in listOf(R.id.disc_q14_a1, R.id.disc_q14_a2, R.id.disc_q14_a3, R.id.disc_q14_a4) -> {
                    if (!q14Values.remove(value)) {
                        Toast.makeText(this, "각 문항 당 1~4 범위 내 숫자를 한 번씩만 입력해 주세요!", Toast.LENGTH_LONG).show()
                        return false
                    }
                }
                in listOf(R.id.disc_q15_a1, R.id.disc_q15_a2, R.id.disc_q15_a3, R.id.disc_q15_a4) -> {
                    if (!q15Values.remove(value)) {
                        Toast.makeText(this, "각 문항 당 1~4 범위 내 숫자를 한 번씩만 입력해 주세요!", Toast.LENGTH_LONG).show()
                        return false
                    }
                }
            }
        }

        return true
    }
}