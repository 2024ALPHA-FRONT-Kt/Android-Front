package com.android.myapplication.ui.bootcamp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.myapplication.R
import com.android.myapplication.databinding.ActivityCampInfo1Binding

class CampInfo1Activity : AppCompatActivity() {
    private lateinit var binding: ActivityCampInfo1Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camp_info1)
        supportActionBar?.hide()
    }
}