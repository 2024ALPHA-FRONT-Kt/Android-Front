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

class DiscTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_disc_test)

        findViewById<Button>(R.id.disc_next_page).setOnClickListener {
            val intent = Intent(this, DiscTest2Activity::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.disc_test_back_button_1).setOnClickListener {
            val intent = Intent(this, DiscActivity::class.java)
            startActivity(intent)
        }
    }
}
