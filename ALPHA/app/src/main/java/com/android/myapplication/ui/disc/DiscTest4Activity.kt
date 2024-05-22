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

class DiscTest4Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_disc_test4)
        supportActionBar?.hide()

        findViewById<Button>(R.id.disc_next_page_4).setOnClickListener {
            if (validateInputs()) {
                val intent = Intent(this, DiscTest5Activity::class.java)
                startActivity(intent)
            }
        }

        findViewById<ImageView>(R.id.disc_test_back_button_4).setOnClickListener {
            val intent = Intent(this, DiscTest3Activity::class.java)
            startActivity(intent)
        }

        val progressBar = findViewById<ProgressBar>(R.id.disc_progress_bar)
        progressBar.updateDiscProBar(10)
    }

    private fun setEditTextInputType() {
        val editTexts = listOf(
            R.id.disc_q7_a1, R.id.disc_q7_a2, R.id.disc_q7_a3, R.id.disc_q7_a4,
            R.id.disc_q8_a1, R.id.disc_q8_a2, R.id.disc_q8_a3, R.id.disc_q8_a4
        )

        for (editTextId in editTexts) {
            val editText = findViewById<EditText>(editTextId)
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
            R.id.disc_q7_a1, R.id.disc_q7_a2, R.id.disc_q7_a3, R.id.disc_q7_a4,
            R.id.disc_q8_a1, R.id.disc_q8_a2, R.id.disc_q8_a3, R.id.disc_q8_a4
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

            val invalidEditTextMessage = when (editTextId) {
                in listOf(R.id.disc_q7_a1, R.id.disc_q7_a2, R.id.disc_q7_a3, R.id.disc_q7_a4) -> {
                    if (!q7Values.remove(value)) {
                        "각 문항 당 1~4 범위 내 숫자를 한 번씩만 입력해 주세요!"
                    } else null
                }
                in listOf(R.id.disc_q8_a1, R.id.disc_q8_a2, R.id.disc_q8_a3, R.id.disc_q8_a4) -> {
                    if (!q8Values.remove(value)) {
                        "각 문항 당 1~4 범위 내 숫자를 한 번씩만 입력해 주세요!"
                    } else null
                }
                else -> null
            }

            if (invalidEditTextMessage != null) {
                Toast.makeText(this, invalidEditTextMessage, Toast.LENGTH_LONG).show()
                return false
            }
        }

        return true
    }
}