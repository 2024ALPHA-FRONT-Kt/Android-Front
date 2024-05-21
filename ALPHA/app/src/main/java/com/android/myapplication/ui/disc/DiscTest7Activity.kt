package com.android.myapplication.ui.disc

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.android.myapplication.R

class DiscTest7Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_disc_test7)

        findViewById<Button>(R.id.disc_next_page_7).setOnClickListener {
            val intent = Intent(this, DiscResultActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.disc_test_back_button_7).setOnClickListener {
            val intent = Intent(this, DiscTest6Activity::class.java)
            startActivity(intent)
        }
    }
}
