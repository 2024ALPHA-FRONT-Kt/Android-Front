package com.android.myapplication.ui.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.myapplication.App
import com.android.myapplication.MainActivity
import com.android.myapplication.R
import com.android.myapplication.api.RetrofitClient
import com.android.myapplication.databinding.ActivityEditUnivBinding
import com.android.myapplication.dto.EditProfile
import com.android.myapplication.dto.ExceptionDto
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EditUnivActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditUnivBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_univ)
        supportActionBar?.hide()

        // 변경 취소하기 버튼
        binding.btnEditCancel.setOnClickListener {
            onBackPressed()
        }

        // api 연결
        val apiService = RetrofitClient.apiservice
        val gson = Gson()

        // token 가져오기
        val globalAccessToken: String = App.prefs.getItem("accessToken","no Token")

        // user정보 가져오기
        val token = "Bearer ${globalAccessToken.replace("\"", "")}"

        val editName = binding.editName.text
        val editUniv = binding.editUniv.text
        val editDepart = binding.editDepart.text


        binding.btnEditSave.setOnClickListener{
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val responseData = apiService.editProfile(token, EditProfile(editName.toString(),editUniv.toString(),editDepart.toString()))
                    Log.e("Response", responseData.toString())

                    Toast.makeText(applicationContext,"정보 수정이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    IntentMain() // 화면 전환
                } catch (e: Exception) {
                    if (e is retrofit2.HttpException){
                        if (e.code() == 404){
                            val errorBody = e.response()?.errorBody()?.string()
                            val errorResponse : ExceptionDto? = gson.fromJson(errorBody, ExceptionDto::class.java)
                            Log.e("404에러: 유저를 찾을 수 없음",errorResponse.toString())
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
    fun IntentMain (){ // 화면전환
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}