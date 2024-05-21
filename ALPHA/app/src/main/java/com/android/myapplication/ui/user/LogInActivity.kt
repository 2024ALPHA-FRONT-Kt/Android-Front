package com.android.myapplication.ui.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.myapplication.MainActivity
import com.android.myapplication.R
import com.android.myapplication.api.RetrofitClient
import com.android.myapplication.databinding.ActivityLogInBinding
import com.android.myapplication.dto.ExceptionDto
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.json.JSONTokener

class LogInActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLogInBinding
    private var waitTime:Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        supportActionBar?.hide() //action바 제거

        // api 연결
        val apiService = RetrofitClient.apiservice
        var globalAccessToken:String = ""
        var globalRefreshToken:String = ""
        val gson = Gson()

        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 로그인 기능
        binding.loginBtn.setOnClickListener{

            //  서버에 로그인 요청
            val userId = binding.userId.text.toString()
            val userPw = binding.userPw.text.toString()
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val responseData = apiService.login(userId,userPw)
                    // 응답 데이터를 사용하여 작업 수행
                    Log.e("Response", responseData.toString())
                    val data = gson.fromJson(responseData.data.toString(),JsonObject::class.java)
                    // 받은 토큰 저장
                    globalAccessToken = data["accessToken"].toString()
                    globalRefreshToken = data["refreshToken"].toString()
                } catch (e: Exception) {
                    if (e is retrofit2.HttpException){
                        if (e.code() == 404){
                            val errorBody = e.response()?.errorBody()?.string()
                            val errorResponse : ExceptionDto? = gson.fromJson(errorBody, ExceptionDto::class.java)
                            Log.e("404에러",errorResponse.toString())
                        }else {
                            Log.e("Error", e.message.toString())
                        }
                    } else {
                        Log.e("Error", e.message.toString())
                    }
                }
            }
            // 화면 전환
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}

