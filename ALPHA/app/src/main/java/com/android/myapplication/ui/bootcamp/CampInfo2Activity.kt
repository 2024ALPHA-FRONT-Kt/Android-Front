package com.android.myapplication.ui.bootcamp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.myapplication.R
import com.android.myapplication.databinding.ActivityCampInfo2Binding

class CampInfo2Activity : AppCompatActivity() {
    private lateinit var binding: ActivityCampInfo2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camp_info2)
        supportActionBar?.hide()
    }
}