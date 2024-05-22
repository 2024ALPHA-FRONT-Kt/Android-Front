package com.android.myapplication.ui.disc

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.android.myapplication.R

class DiscActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_disc)
        supportActionBar?.hide()

        findViewById<Button>(R.id.disc_test_start_button).setOnClickListener {
            val intent = Intent(this, DiscTestActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.disc_start_page_back).setOnClickListener {
            val intent = // todo
            startActivity(intent)
        }
    }
}
