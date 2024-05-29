package com.android.myapplication.ui.bootcamp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.android.myapplication.R
import com.android.myapplication.databinding.ActivityCampInfo1Binding
import com.google.android.material.tabs.TabLayoutMediator

class CampInfo1Activity : AppCompatActivity() {
    private lateinit var binding: ActivityCampInfo1Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camp_info1)
        supportActionBar?.hide()

        // 바인딩
        binding = ActivityCampInfo1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.campInfo1Back.setOnClickListener {
            onBackPressed()
        }

        //ViewPager2 Adapter 셋팅
        var viewPager2Adatper = Info1Adapter(this)
        viewPager2Adatper.addFragment(Info1Tab1Fragment())
        viewPager2Adatper.addFragment(Info1Tab2Fragment())
        viewPager2Adatper.addFragment(Info1Tab3Fragment())

        //Adapter 연결
        binding.vpViewpagerMain.apply {
            adapter = viewPager2Adatper

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                }
            })
        }

        //ViewPager, TabLayout 연결
        TabLayoutMediator(binding.tlNavigationView, binding.vpViewpagerMain) { tab, position ->
            when (position) {
                0 -> tab.text = "강의정보"
                1 -> tab.text = "강사정보"
                2 -> tab.text = "메뉴얼"
            }
        }.attach()

    }
}