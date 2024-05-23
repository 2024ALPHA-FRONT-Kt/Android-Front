package com.android.myapplication.ui.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.myapplication.App
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        supportActionBar?.hide() //action바 제거

        // api 연결
        val apiService = RetrofitClient.apiservice
        val gson = Gson()

        // 로그인 확인용
        var loginBool = 0

        val userId = binding.userId.text.toString()
        val userPw = binding.userPw.text.toString()

        // 로그인 기능
        binding.loginBtn.setOnClickListener{
            //  서버에 로그인 요청
            // editText가 비어있는지 확인
            if (userId.trim().isEmpty()){
                Toast.makeText(applicationContext,"아이디를 입력해 주세요", Toast.LENGTH_SHORT).show()
            }
            if (userPw.trim().isEmpty()){
                Toast.makeText(applicationContext,"비밀번호를 입력해 주세요", Toast.LENGTH_SHORT).show()
            }
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val responseData = apiService.login(userId,userPw)
                    // 응답 데이터를 사용하여 작업 수행
                    Log.e("Response", responseData.toString())
                    val data = gson.fromJson(responseData.data.toString(),JsonObject::class.java)
                    // 받은 토큰 저장
                    App.prefs.addItem("accessToken", data["accessToken"].toString()) // access
                    App.prefs.addItem("refreshToken", data["refreshToken"].toString()) // refresh
                    Log.e("token",data["accessToken"].toString())
                    loginBool = 1 // 로그인 성공 여부 적용
                    IntentAct(loginBool) // 화면 전환
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
        }
    }
    fun IntentAct (loginBool:Int){ // 화면전환
        if (loginBool ==1 ){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(applicationContext,"아이디와 비밀번호를 확인해주세요",Toast.LENGTH_SHORT).show()
        }
    }
}

