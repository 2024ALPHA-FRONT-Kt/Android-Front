package com.android.myapplication.ui.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.myapplication.R
import com.android.myapplication.api.RetrofitClient
import com.android.myapplication.databinding.ActivityEditBinding
import com.android.myapplication.databinding.ActivityStep2Univ2Binding
import com.android.myapplication.dto.ExceptionDto
import com.android.myapplication.dto.SignInProfile
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Step2Univ2Activity : AppCompatActivity() {
    private lateinit var binding: ActivityStep2Univ2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step2_univ2)
        supportActionBar?.hide()

        // 바인딩
        binding = ActivityStep2Univ2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        // api 연결
        val apiService = RetrofitClient.apiservice
        val gson = Gson()

        val userRole = "UNIV"
        // 성별 확인용
        val newMan = binding.newMan
        val newWoman = binding.newWoman
        var newGender = "" // 성별

        // 성별 확인
        binding.radioGroup.setOnCheckedChangeListener{ group, checkedId ->
            when(checkedId) {
                newMan.id -> newGender = "남자"
                newWoman.id -> newGender = "여자"
            }
            Log.e("성별임!!!!!",newGender)
        }

        // 회원가입하기
        binding.btnNext.setOnClickListener {

            // data
            val newName = binding.newName.text.toString()    // 이름
            val newEmail = intent.getStringExtra("email").toString() // 이메일
            val age = binding.newAge.text.toString()  // 나이
            var newAge = 0

            val newId = binding.newId.text.toString() // 아이디
            val newPw = binding.newPw.text.toString() // 비밀번호
            val newPwRe = binding.newPwRe.text.toString()
            val newUnivH = intent.getStringExtra("univ").toString() // 재학대학
            val newDepartH = binding.newDepartU.text.toString() // 재학학과

            var bool = 0

            // editText가 비어있는지 확인
            if (newName.trim().isEmpty() || newEmail.trim().isEmpty() || age.trim().isEmpty() || newId.trim().isEmpty() || newPw.trim().isEmpty() || newPwRe.trim().isEmpty() || newUnivH.trim().isEmpty() || newDepartH.trim().isEmpty()){
                Toast.makeText(applicationContext,"압력을 완료해 주세요", Toast.LENGTH_SHORT).show()
            } else if (newPw != newPwRe){
                Toast.makeText(applicationContext,"비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            } else if (newGender == "") {
                    Toast.makeText(applicationContext,"성별을 입력해 주세요", Toast.LENGTH_SHORT).show()
            } else {
                newAge = age.toInt()
                // 서버에 전송
                GlobalScope.launch(Dispatchers.IO) {
                    Log.e("ddddd",
                        SignInProfile(userRole,newName,newEmail,newGender,newAge,newId,newPw,null,newUnivH,newDepartH,null).toString()
                    )
                    try {
                        val responseData = apiService.signIn(SignInProfile(userRole,newName,newEmail,newGender,newAge,newId,newPw,null,newUnivH,newDepartH,null))
                        Log.e("Response", responseData.toString())
                        intentLogin()
                    } catch (e: Exception) {
                        if (e is retrofit2.HttpException){
                            if (e.code() == 400){
                                val errorBody = e.response()?.errorBody()?.string()
                                val errorResponse : ExceptionDto? = gson.fromJson(errorBody, ExceptionDto::class.java)
                                Log.e("400에러 아이디 중복으로 인한 실패",errorResponse.toString())
                                runOnUiThread{ Toast.makeText(applicationContext,"중복된 아이디 입니다.", Toast.LENGTH_SHORT).show() }
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
    }
    private fun intentLogin() {
        val intent = Intent(this, LogInActivity::class.java)
        startActivity(intent)
    }
}