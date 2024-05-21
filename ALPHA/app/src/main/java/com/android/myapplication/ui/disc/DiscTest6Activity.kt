package com.android.myapplication.ui.disc

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.myapplication.R

class DiscTest6Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_disc_test6)

        findViewById<Button>(R.id.disc_next_page_6).setOnClickListener {
            val intent = Intent(this, DiscTest7Activity::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.disc_test_back_button_6).setOnClickListener {
            val intent = Intent(this, DiscTest5Activity::class.java)
            startActivity(intent)
        }
    }
}
