package com.android.myapplication.ui.user

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.myapplication.App
import com.android.myapplication.R
import com.android.myapplication.api.RetrofitClient
import com.android.myapplication.databinding.ActivityEditBinding
import com.android.myapplication.databinding.ActivityStep2HighBinding
import com.android.myapplication.dto.ExceptionDto
import com.android.myapplication.dto.SignInProfile
import com.google.gson.Gson
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
        var newGender = "" // 성별
        val phone = null
        val image = null

        // 회원가입하기
        binding.btnNext.setOnClickListener {

        // data
            val newName = binding.newName.text.toString()    // 이름
            val newEmail = binding.newEmail.text.toString()  // 이메일
            val age = binding.newAge.text.toString()  // 나이
            var newAge = 0

            // 성별 확인용
            val newMan = binding.newMan
            val newWoman = binding.newWoman

//            var newGender = "" // 성별
            val newId = binding.newId.text.toString() // 아이디
            val newPw = binding.newPw.text.toString() // 비밀번호
            val newPwRe = binding.newPwRe.text.toString()
            val newUnivH = binding.newUnivH.text.toString() // 지망대학
            val newDepartH = binding.newDepartH.text.toString() // 지망학과
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
                Toast.makeText(applicationContext,"희망 대학을 입력해 주세요", Toast.LENGTH_SHORT).show()
            }
            if (newDepartH.trim().isEmpty()){
                Toast.makeText(applicationContext,"희망 학과를 입력해 주세요", Toast.LENGTH_SHORT).show()
            }

            // 성별 확인
            if (newMan.isChecked == false && newWoman.isChecked == false) {
                Toast.makeText(applicationContext,"성별을 입력해 주세요", Toast.LENGTH_SHORT).show()
            }

            binding.radioGroup.setOnCheckedChangeListener{ group, checkedId ->
                when(checkedId) {
                    newMan.id -> newGender = "남자"
                    newWoman.id -> newGender = "여자"
                }
                Log.e("성별",newGender)
            }

            // 비밀번호 일치 확인
            if (newPw != newPwRe){
                Toast.makeText(applicationContext,"비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            }
            Log.e("보낸거1","$userRole $newName $newEmail $newGender $newAge $newId $newPw $phone $newUnivH $newDepartH $image")
            // 서버에 전송
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    Log.e("요청은하니?","$userRole $newName $newEmail $newGender $newAge $newId $newPw $phone $newUnivH $newDepartH $image")
                    val responseData = apiService.signIn(SignInProfile( userRole,newName,newEmail,newGender,newAge,newId,newPw,phone,newUnivH,newDepartH,image))
                    Log.e("보낸거2","$userRole $newName $newEmail $newGender $newAge $newId $newPw $phone $newUnivH $newDepartH $image")
                    Log.e("Response", responseData.toString())
                } catch (e: Exception) {
                    if (e is retrofit2.HttpException){
                        val errorBody = e.response()?.errorBody()?.string()
                        val errorResponse : ExceptionDto? = gson.fromJson(errorBody, ExceptionDto::class.java)
                        if (e.code() == 400){
//                            val errorBody = e.response()?.errorBody()?.string()
//                            val errorResponse : ExceptionDto? = gson.fromJson(errorBody, ExceptionDto::class.java)
                            Log.e("400에러 아이디 중복으로 인한 실패",errorResponse.toString())
                            Toast.makeText(applicationContext,"중복된 아이디 입니다.", Toast.LENGTH_SHORT).show()
                        }else {
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