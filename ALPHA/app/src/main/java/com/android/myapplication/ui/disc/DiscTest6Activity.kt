package com.android.myapplication.ui.disc

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.myapplication.R

class DiscTest6Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_disc_test6)

        setEditTextInputType()

        findViewById<Button>(R.id.disc_next_page_6).setOnClickListener {
            if (validateInputs()) {
                val intent = Intent(this, DiscTest7Activity::class.java)
                startActivity(intent)
            }
        }

        findViewById<ImageView>(R.id.disc_test_back_button_6).setOnClickListener {
            val intent = Intent(this, DiscTest5Activity::class.java)
            startActivity(intent)
        }
    }

    private fun setEditTextInputType() {
        val editTexts = listOf(
            R.id.disc_q11_a1, R.id.disc_q11_a2, R.id.disc_q11_a3, R.id.disc_q11_a4,
            R.id.disc_q12_a1, R.id.disc_q12_a2, R.id.disc_q12_a3, R.id.disc_q12_a4,
            R.id.disc_q13_a1, R.id.disc_q13_a2, R.id.disc_q13_a3, R.id.disc_q13_a4
        )

        for (editTextId in editTexts) {
            val editText = findViewById<EditText>(editTextId)
            editText.inputType = InputType.TYPE_CLASS_NUMBER
            editText.filters = arrayOf(InputFilter.LengthFilter(1))
        }
    }

    private fun validateInputs(): Boolean {
        val q11Values = mutableSetOf(1, 2, 3, 4)
        val q12Values = mutableSetOf(1, 2, 3, 4)
        val q13Values = mutableSetOf(1, 2, 3, 4)

        val editTexts = listOf(
            R.id.disc_q11_a1, R.id.disc_q11_a2, R.id.disc_q11_a3, R.id.disc_q11_a4,
            R.id.disc_q12_a1, R.id.disc_q12_a2, R.id.disc_q12_a3, R.id.disc_q12_a4,
            R.id.disc_q13_a1, R.id.disc_q13_a2, R.id.disc_q13_a3, R.id.disc_q13_a4
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
                in listOf(R.id.disc_q11_a1, R.id.disc_q11_a2, R.id.disc_q11_a3, R.id.disc_q11_a4) -> {
                    if (!q11Values.remove(value)) {
                        Toast.makeText(this, "각 문항 당 1~4 범위 내 숫자를 한 번씩만 입력해 주세요!", Toast.LENGTH_LONG).show()
                        return false
                    }
                }
                in listOf(R.id.disc_q12_a1, R.id.disc_q12_a2, R.id.disc_q12_a3, R.id.disc_q12_a4) -> {
                    if (!q12Values.remove(value)) {
                        Toast.makeText(this, "각 문항 당 1~4 범위 내 숫자를 한 번씩만 입력해 주세요!", Toast.LENGTH_LONG).show()
                        return false
                    }
                }
                in listOf(R.id.disc_q13_a1, R.id.disc_q13_a2, R.id.disc_q13_a3, R.id.disc_q13_a4) -> {
                    if (!q13Values.remove(value)) {
                        Toast.makeText(this, "각 문항 당 1~4 범위 내 숫자를 한 번씩만 입력해 주세요!", Toast.LENGTH_LONG).show()
                        return false
                    }
                }
            }
        }

        return true
    }
}