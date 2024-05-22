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

class DiscTest3Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_disc_test3)
        supportActionBar?.hide()

        setEditTextInputType()

        findViewById<Button>(R.id.disc_next_page_3).setOnClickListener {
            if (validateInputs()) {
                val intent = Intent(this, DiscTest4Activity::class.java)
                startActivity(intent)
            }
        }

        findViewById<ImageView>(R.id.disc_test_back_button_3).setOnClickListener {
            val intent = Intent(this, DiscActivity::class.java)
            startActivity(intent)
        }

        val progressBar = findViewById<ProgressBar>(R.id.disc_progress_bar)
        progressBar.updateDiscProBar(10)
    }

    private fun setEditTextInputType() {
        val editTexts = listOf(
            R.id.disc_q5_a1, R.id.disc_q5_a2, R.id.disc_q5_a3, R.id.disc_q5_a4,
            R.id.disc_q6_a1, R.id.disc_q6_a2, R.id.disc_q6_a3, R.id.disc_q6_a4
        )

        for (editTextId in editTexts) {
            val editText = findViewById<EditText>(editTextId)
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
            R.id.disc_q5_a1, R.id.disc_q5_a2, R.id.disc_q5_a3, R.id.disc_q5_a4,
            R.id.disc_q6_a1, R.id.disc_q6_a2, R.id.disc_q6_a3, R.id.disc_q6_a4
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

            if (editTextId in listOf(R.id.disc_q5_a1, R.id.disc_q5_a2, R.id.disc_q5_a3, R.id.disc_q5_a4)) {
                if (!q3Values.remove(value)) {
                    Toast.makeText(this, "각 문항 당 1~4 범위 내 숫자를 한 번씩만 입력해 주세요!", Toast.LENGTH_LONG).show()
                    return false
                }
            } else if (editTextId in listOf(R.id.disc_q6_a1, R.id.disc_q6_a2, R.id.disc_q6_a3, R.id.disc_q6_a4)) {
                if (!q4Values.remove(value)) {
                    Toast.makeText(this, "각 문항 당 1~4 범위 내 숫자를 한 번씩만 입력해 주세요!", Toast.LENGTH_LONG).show()
                    return false
                }
            }
        }

        return true
    }
}