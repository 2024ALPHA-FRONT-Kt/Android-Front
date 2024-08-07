package com.android.myapplication.ui.user

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.myapplication.App
import com.android.myapplication.R
import com.android.myapplication.api.RetrofitClient
import com.android.myapplication.databinding.ActivityStep2HighBinding
import com.android.myapplication.dto.ExceptionDto
import com.android.myapplication.dto.SignInProfile
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Step2HighActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStep2HighBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step2_high)
        supportActionBar?.hide()

        // 바인딩
        binding = ActivityStep2HighBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // api 연결
        val apiService = RetrofitClient.apiservice
        val gson = Gson()

        val userRole = "HIGH"
        val phone = null
        val image = null
        // 성별 확인용
        val newMan = binding.newMan
        val newWoman = binding.newWoman
        var newGender = "" // 성별

        // 성별 확인
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                newMan.id -> newGender = "남자"
                newWoman.id -> newGender = "여자"
            }
            Log.e("성별임!!!!!", newGender)
        }

        binding.btnDuplication.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    // token 가져오기
                    val globalAccessToken: String = App.prefs.getItem("accessToken", "no Token")
                    // user정보 가져오기
                    val userId = App.prefs.getItem("userId","noUserID")
                    val token = "Bearer ${globalAccessToken.replace("\"", "")}"
                    val responseData = apiService.validation(token,userId)
                    Log.e("Response", responseData.toString())
                    val data = gson.fromJson(responseData.data.toString(), JsonElement::class.java)
                    if (data.toString() == "true"){
                        binding.btnDuplication.isEnabled = false
                        binding.btnDuplication.setTextColor(Color.parseColor("#D9D9D9"))
                        runOnUiThread {
                            Toast.makeText(
                                applicationContext,
                                "사용가능한 아이디입니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } catch (e: Exception) {
                    if (e is retrofit2.HttpException) {
                        if (e.code() == 400) {
                            val errorBody = e.response()?.errorBody()?.string()
                            val errorResponse: ExceptionDto? =
                                gson.fromJson(errorBody, ExceptionDto::class.java)
                            Log.e("400에러", errorResponse.toString())
                            runOnUiThread {
                                Toast.makeText(
                                    applicationContext,
                                    "중복된 아이디 입니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Log.e("Error", e.message.toString())
                            runOnUiThread {
                                Toast.makeText(
                                    applicationContext,
                                    "다시 확인해 주세요",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } else {
                        Log.e("Error", e.message.toString())
                        runOnUiThread {
                            Toast.makeText(
                                applicationContext,
                                "다시 확인해 주세요",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

        // 회원가입하기
        binding.btnNext.setOnClickListener {

            // data
            val newName = binding.newName.text.toString()    // 이름
            val newEmail = binding.newEmail.text.toString()  // 이메일
            val age = binding.newAge.text.toString()  // 나이
            var newAge = 0
            val newId = binding.newId.text.toString() // 아이디
            val newPw = binding.newPw.text.toString() // 비밀번호
            val newPwRe = binding.newPwRe.text.toString()
            val newUnivH = binding.newUnivH.text.toString() // 지망대학
            val newDepartH = binding.newDepartH.text.toString() // 지망학과

            // editText가 비어있는지 확인
            if (newName.trim().isEmpty() || newEmail.trim().isEmpty() || age.trim()
                    .isEmpty() || newId.trim().isEmpty() || newPw.trim().isEmpty() || newPwRe.trim()
                    .isEmpty() || newUnivH.trim().isEmpty() || newDepartH.trim().isEmpty()
            ) {
                Toast.makeText(applicationContext, "압력을 완료해 주세요", Toast.LENGTH_SHORT).show()
            } else if (newPw != newPwRe) {
                Toast.makeText(applicationContext, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            } else if (newGender == "") {
                Toast.makeText(applicationContext, "성별을 입력해 주세요", Toast.LENGTH_SHORT).show()
            } else {
                newAge = age.toInt()
                // 서버에 전송
                GlobalScope.launch(Dispatchers.IO) {
                    try {
                        val responseData = apiService.signIn(
                            SignInProfile(
                                userRole,
                                newName,
                                newEmail,
                                newGender,
                                newAge,
                                newId,
                                newPw,
                                phone,
                                newUnivH,
                                newDepartH,
                                image
                            )
                        )
                        Log.e("Response", responseData.toString())
                        runOnUiThread {
                            Toast.makeText(
                                applicationContext,
                                "회원가입이 완료되었습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        intentLogin()
                    } catch (e: Exception) {
                        if (e is retrofit2.HttpException) {
                            val errorBody = e.response()?.errorBody()?.string()
                            val errorResponse: ExceptionDto? =
                                gson.fromJson(errorBody, ExceptionDto::class.java)
                            if (e.code() == 400) {
                                Log.e("400에러 아이디 중복으로 인한 실패", errorResponse.toString())
                                runOnUiThread {
                                    Toast.makeText(
                                        applicationContext,
                                        "중복된 아이디 입니다.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                // 서버가 만들어둔 에러처리
                                Log.e("Error", errorResponse.toString())
                            }
                        } else {
                            Log.e("Error", e.message.toString())
                        }
                    }
                }
            }
        }
    }

    private fun intentLogin() {
        val intent = Intent(this, LogInActivity::class.java)
        startActivity(intent)
    }
}