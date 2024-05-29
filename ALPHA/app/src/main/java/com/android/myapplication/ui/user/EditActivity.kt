package com.android.myapplication.ui.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.myapplication.App
import com.android.myapplication.MainActivity
import com.android.myapplication.R
import com.android.myapplication.api.RetrofitClient
import com.android.myapplication.databinding.ActivityEditBinding
import com.android.myapplication.dto.EditProfile
import com.android.myapplication.dto.ExceptionDto
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        supportActionBar?.hide()

        // 바인딩
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // api 연결
        val apiService = RetrofitClient.apiservice
        val gson = Gson()

        // token 가져오기
        val globalAccessToken: String = App.prefs.getItem("accessToken", "no Token")

        // user정보 가져오기
        val token = "Bearer ${globalAccessToken.replace("\"", "")}"
        val userR = App.prefs.getItem("userRole", "noUserRole")


        // 조건에 따른 text 변경
        if (userR == "UNIV") {
            binding.txtEditUniv.text = "재학 대학"
            binding.txtEditDepart.text = "재학 학과"

        } else { // HIGH
            binding.txtEditUniv.text = "희망 대학"
            binding.txtEditDepart.text = "희망 학과"
        }

        // 변경 취소하기 버튼
        binding.btnEditCancel.setOnClickListener {
            onBackPressed()
        }

        binding.btnEditSave.setOnClickListener {
            val editName = binding.editName.text.toString()
            val editUniv = binding.editUniv.text.toString()
            val editDepart = binding.editDepart.text.toString()
            // editText가 비어있는지 확인
            if (editName.trim().isEmpty() || editUniv.trim().isEmpty() || editDepart.trim()
                    .isEmpty()
            ) {
                Toast.makeText(applicationContext, "입력을 완료해 주세요", Toast.LENGTH_SHORT).show()
            } else {
                GlobalScope.launch(Dispatchers.IO) {
                    try {
                        val responseData = apiService.editProfile(
                            token,
                            EditProfile(editName, editUniv, editDepart, null)
                        )
                        Log.e("Response", responseData.toString())
                        IntentMain() // 화면 전환
                    } catch (e: Exception) {
                        if (e is retrofit2.HttpException) {
                            if (e.code() == 404) {
                                val errorBody = e.response()?.errorBody()?.string()
                                val errorResponse: ExceptionDto? =
                                    gson.fromJson(errorBody, ExceptionDto::class.java)
                                Log.e("404에러: 유저를 찾을 수 없음", errorResponse.toString())
                            } else {
                                val errorBody = e.response()?.errorBody()?.string()
                                val errorResponse: ExceptionDto? =
                                    gson.fromJson(errorBody, ExceptionDto::class.java)
                                Log.e("에러", errorResponse.toString())
                            }
                        } else {
                            Log.e("Error", e.message.toString())
                        }
                    }
                }
            }
        }
    }

    fun IntentMain() { // 화면전환
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}