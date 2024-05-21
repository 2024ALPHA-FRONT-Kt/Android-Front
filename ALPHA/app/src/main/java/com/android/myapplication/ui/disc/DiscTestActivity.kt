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

class DiscTestActivity : AppCompatActivity() {

    var discScore = DiscScore(DScore = 0, IScore = 0, SScore = 0, CScore = 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_disc_test)

        setEditTextInputType()

        findViewById<Button>(R.id.disc_next_page).setOnClickListener {
            if (validateInputs()) {
                val intent = Intent(this, DiscTest2Activity::class.java)
                startActivity(intent)
            }
        }

        findViewById<ImageView>(R.id.disc_test_back_button_1).setOnClickListener {
            val intent = Intent(this, DiscActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setEditTextInputType() {
        val editTexts = listOf(
            R.id.disc_q1_a1, R.id.disc_q1_a2, R.id.disc_q1_a3, R.id.disc_q1_a4,
            R.id.disc_q2_a1, R.id.disc_q2_a2, R.id.disc_q2_a3, R.id.disc_q2_a4
        )

        for (editTextId in editTexts) {
            val editText = findViewById<EditText>(editTextId)
            editText.inputType = InputType.TYPE_CLASS_NUMBER
            editText.filters = arrayOf(InputFilter.LengthFilter(1))
        }
    }

    private fun validateInputs(): Boolean {
        val q1Values = mutableSetOf<Int>()
        val q2Values = mutableSetOf<Int>()

        for (i in 1..4) {
            q1Values.add(i)
            q2Values.add(i)
        }

        val editTexts = listOf(
            R.id.disc_q1_a1, R.id.disc_q1_a2, R.id.disc_q1_a3, R.id.disc_q1_a4,
            R.id.disc_q2_a1, R.id.disc_q2_a2, R.id.disc_q2_a3, R.id.disc_q2_a4
        )

        for (editTextId in editTexts) {
            val editText = findViewById<EditText>(editTextId)
            val text = editText.text.toString()

            if (text.isEmpty()) {
                Toast.makeText(this, "모든 문항에 숫자를 입력해 주세요", Toast.LENGTH_LONG).show()
                return false
            }

            val value = text.toIntOrNull()
            if (value == null || value !in 1..4) {
                Toast.makeText(this, "1~4 범위 내의 숫자를 입력해 주세요", Toast.LENGTH_LONG).show()
                return false
            }

            if (editTextId in listOf(R.id.disc_q1_a1, R.id.disc_q1_a2, R.id.disc_q1_a3, R.id.disc_q1_a4)) {
                if (!q1Values.remove(value)) {
                    Toast.makeText(this, "각 문항 당 1~4 범위 내 숫자를 한 번씩만 입력해 주세요", Toast.LENGTH_LONG).show()
                    return false
                }
            } else if (editTextId in listOf(R.id.disc_q2_a1, R.id.disc_q2_a2, R.id.disc_q2_a3, R.id.disc_q2_a4)) {
                if (!q2Values.remove(value)) {
                    Toast.makeText(this, "각 문항 당 1~4 범위 내 숫자를 한 번씩만 입력해 주세요", Toast.LENGTH_LONG).show()
                    return false
                }
            }
        }

        return true
    }
}