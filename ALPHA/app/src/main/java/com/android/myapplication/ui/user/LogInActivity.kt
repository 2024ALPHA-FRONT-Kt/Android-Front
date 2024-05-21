package com.android.myapplication.ui.user

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.myapplication.R

class LogInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        supportActionBar?.hide()
    }
}