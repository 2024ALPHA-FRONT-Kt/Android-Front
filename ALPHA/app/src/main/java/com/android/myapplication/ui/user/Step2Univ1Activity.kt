package com.android.myapplication.ui.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.myapplication.R
import com.android.myapplication.api.RetrofitClient
import com.android.myapplication.databinding.ActivityStep2Univ1Binding
import com.android.myapplication.dto.UnivCert.Certify
import com.android.myapplication.dto.UnivCert.CertifyCode
import com.android.myapplication.dto.UnivCert.Check
import com.android.myapplication.dto.UnivCert.Clear
import com.android.myapplication.dto.UnivCert.UnivCertException
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Step2Univ1Activity : AppCompatActivity() {
    private lateinit var binding: ActivityStep2Univ1Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step2_univ1)
        supportActionBar?.hide()

        // 바인딩
        binding = ActivityStep2Univ1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // api 연결
        val univService = RetrofitClient.univcertservice
        val gson = Gson()
        val apiKey = "385e29ed-775f-43cb-a41e-24d13abe3516"
        var univCheck = false

        binding.univEmail.visibility = View.INVISIBLE
        binding.passkey.visibility = View.INVISIBLE
        // 초기화
        binding.btnClear.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val responseData = univService.clear(Clear(apiKey))
                    Log.e("clear했음", responseData.toString())
                } catch (e: Exception) {
                    Log.e("Error", e.message.toString())
                }
            }
        }
        // 대학확인
        binding.btnGetUnivCheck.setOnClickListener {
            val newUniv = binding.newUnivU.text.toString()
            Log.e("newUniv", newUniv)
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val responseData = univService.check(Check(newUniv))
                    Log.e("대학 확인 성공", responseData.toString())
                    univCheck = true
                    binding.root.post {
                        Toast.makeText(applicationContext, "대학 확인 완료", Toast.LENGTH_SHORT).show()
                        binding.newUnivU.isEnabled = false
                        binding.univEmail.visibility = View.VISIBLE
                    }
                } catch (e: Exception) {
                    if (e is retrofit2.HttpException) {
                        if (e.code() == 400) {
                            val errorBody = e.response()?.errorBody()?.string()
                            val errorResponse: UnivCertException? =
                                gson.fromJson(errorBody, UnivCertException::class.java)
                            Log.e("400에러", errorResponse.toString())
                        } else {
                            Log.e("Error", e.message.toString())
                        }
                    } else {
                        Log.e("Error", e.message.toString())
                    }
                    Log.e("Error", e.message.toString())
                    runOnUiThread {
                        Toast.makeText(
                            applicationContext,
                            "올바른 대학명인지, 22년 입학생 수 상위 150개 이내에 드는 학교인지 확인해주세요.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }

        // 인증번호 발송
        binding.btnGetPasskey.setOnClickListener {
            val univ = binding.newUnivU.text.toString()
            val email = binding.newEmail.text.toString()
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val responseData = univService.certify(Certify(apiKey, email, univ, univCheck))
                    Log.e("인증 번호 발송 성공", responseData.toString())
                    binding.root.post {
                        runOnUiThread {
                            Toast.makeText(
                                applicationContext,
                                "인증 번호 발송 성공",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        binding.univEmail.isEnabled = false
                        binding.passkey.visibility = View.VISIBLE
                    }

                } catch (e: Exception) {
                    Log.e("Error", e.message.toString())
                    runOnUiThread {
                        Toast.makeText(
                            applicationContext,
                            "인증 번호 발송 실패",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }

        var valid = 0 // 인증결과
        binding.btnCheckPasskey.setOnClickListener {
            val univ = binding.newUnivU.text.toString()
            val email = binding.newEmail.text.toString()
            val code = binding.code.text.toString().toInt()
            GlobalScope.launch(Dispatchers.IO) {
                try { // 인증잘되면
                    val responseData =
                        univService.certifyCode(CertifyCode(apiKey, email, univ, code))
                    Log.e("인증 성공", responseData.toString())
                    valid = 1
                    binding.root.post {
                        binding.passkey.isEnabled = false
                        runOnUiThread {
                            Toast.makeText(
                                applicationContext,
                                "대학 인증 완료",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                } catch (e: Exception) {
                    Log.e("Error", e.message.toString())
                    runOnUiThread {
                        Toast.makeText(
                            applicationContext,
                            "이메일 인증을 다시 해주세요",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        binding.btnNext.setOnClickListener {
            if (valid == 1) {
                val intent = Intent(this, Step2Univ2Activity::class.java)
                intent.putExtra("univ", binding.newUnivU.text.toString())
                intent.putExtra("email", binding.newEmail.text.toString())
                startActivity(intent)
            } else {
                Toast.makeText(applicationContext, "이메일 인증이 완료되지 않았습니다.", Toast.LENGTH_SHORT).show()
            }
        }

    }
}