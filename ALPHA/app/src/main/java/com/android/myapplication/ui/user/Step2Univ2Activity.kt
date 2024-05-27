package com.android.myapplication.ui.user

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

        // 회원가입하기
        binding.btnNext.setOnClickListener {

            // data
            val newName = binding.newName.toString()    // 이름
            val newEmail = intent.getStringExtra("email").toString() // 이메일
            val age = binding.newAge.toString()  // 나이
            var newAge = 0

            // 성별 확인용
            val newMan = binding.newMan
            val newWoman = binding.newWoman

            var newGender = "" // 성별
            val newId = binding.newId.toString() // 아이디
            val newPw = binding.newPw.toString() // 비밀번호
            val newPwRe = binding.newPwRe.toString()
            val newUnivH = intent.getStringExtra("univ").toString() // 재학대학
            val newDepartH = binding.newDepartU.toString() // 재학학과
            // data

            // editText가 비어있는지 확인
            if (newName.trim().isEmpty()){
                Toast.makeText(applicationContext,"이름을 입력해 주세요", Toast.LENGTH_SHORT).show()
            }
            if (newEmail.trim().isEmpty()){
                Toast.makeText(applicationContext,"이메일을 입력해 주세요", Toast.LENGTH_SHORT).show()
            }
            if (age.trim().isEmpty()){
                Toast.makeText(applicationContext,"나이를 입력해 주세요", Toast.LENGTH_SHORT).show()
            } else {
                newAge = age.toInt()
            }
            if (newId.trim().isEmpty()){
                Toast.makeText(applicationContext,"아이디를 입력해 주세요", Toast.LENGTH_SHORT).show()
            }
            if (newPw.trim().isEmpty()){
                Toast.makeText(applicationContext,"비밀번호를 입력해 주세요", Toast.LENGTH_SHORT).show()
            }
            if (newPwRe.trim().isEmpty()){
                Toast.makeText(applicationContext,"비밀번호 확인을 입력해 주세요", Toast.LENGTH_SHORT).show()
            }
            if (newUnivH.trim().isEmpty()){
                Toast.makeText(applicationContext,"재학 대학을 입력해 주세요", Toast.LENGTH_SHORT).show()
            }
            if (newDepartH.trim().isEmpty()){
                Toast.makeText(applicationContext,"재학 학과를 입력해 주세요", Toast.LENGTH_SHORT).show()
            }
            // 성별 확인
            binding.radioGroup.setOnCheckedChangeListener{ group, checkedId ->
                when(checkedId) {
                    newMan.id -> newGender = "남자"
                    newWoman.id -> newGender = "여자"
                }
                if (checkedId != newMan.id && checkedId != newWoman.id) {
                    Toast.makeText(applicationContext,"성별을 입력해 주세요", Toast.LENGTH_SHORT).show()
                }
            }
            // 비밀번호 일치 확인
            if (newPw != newPwRe){
                Toast.makeText(applicationContext,"비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            }
            // 서버에 전송
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val responseData = apiService.signIn(SignInProfile(userRole,newName,newEmail,newGender,newAge,newId,newPw,null,newUnivH,newDepartH,null))
                    Log.e("Response", responseData.toString())
                } catch (e: Exception) {
                    if (e is retrofit2.HttpException){
                        if (e.code() == 400){
                            val errorBody = e.response()?.errorBody()?.string()
                            val errorResponse : ExceptionDto? = gson.fromJson(errorBody, ExceptionDto::class.java)
                            Log.e("400에러 아이디 중복으로 인한 실패",errorResponse.toString())
                            Toast.makeText(applicationContext,"중복된 아이디 입니다.", Toast.LENGTH_SHORT).show()
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