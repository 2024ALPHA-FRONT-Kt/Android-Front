package com.android.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.android.myapplication.api.RetrofitClient
import com.android.myapplication.databinding.ActivityMainBinding
import com.android.myapplication.dto.ExceptionDto
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var waitTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // navigation
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_mypage
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // api 연결
        val apiService = RetrofitClient.apiservice
        val gson = Gson()

        // token 가져오기
        val globalAccessToken: String = App.prefs.getItem("accessToken", "no Token")

        // user정보 가져오기
        val token = "Bearer ${globalAccessToken.replace("\"", "")}"

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val responseData = apiService.myPage(token)
                Log.e("Response", responseData.toString())
                val data = gson.fromJson(responseData.data.toString(), JsonObject::class.java)

                // userRole 저장
                App.prefs.addItem("userRole", data["userRole"].toString().replace("\"", ""))
            } catch (e: Exception) {
                if (e is retrofit2.HttpException) {
                    if (e.code() == 404) {
                        val errorBody = e.response()?.errorBody()?.string()
                        val errorResponse: ExceptionDto? =
                            gson.fromJson(errorBody, ExceptionDto::class.java)
                        Log.e("400에러 유저를 찾을 수 없음", errorResponse.toString())
                    } else {
                        Log.e("Error", e.message.toString())
                    }
                } else {
                    Log.e("Error", e.message.toString())
                }
            }
        }

    }

    override fun onBackPressed() {
        if (waitTime + 3000 > System.currentTimeMillis()) {
            super.onBackPressed()
            finish()
        } else {
            Toast.makeText(applicationContext, "뒤로가기를 한번 더 누르면 종료합니다", Toast.LENGTH_SHORT).show()
        }
        waitTime = System.currentTimeMillis()
    }
}