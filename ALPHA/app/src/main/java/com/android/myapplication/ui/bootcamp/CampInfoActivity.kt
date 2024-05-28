package com.android.myapplication.ui.bootcamp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.myapplication.R
import com.android.myapplication.databinding.ActivityCampInfoBinding

class CampInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCampInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camp_info)
        supportActionBar?.hide()
    }
}