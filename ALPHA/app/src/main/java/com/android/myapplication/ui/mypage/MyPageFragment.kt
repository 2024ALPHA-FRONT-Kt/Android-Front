package com.android.myapplication.ui.mypage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.myapplication.App
import com.android.myapplication.api.RetrofitClient
import com.android.myapplication.databinding.FragmentMypageBinding
import com.android.myapplication.dto.ExceptionDto
import com.android.myapplication.ui.user.EditHighActivity
import com.android.myapplication.ui.user.EditUnivActivity
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MyPageFragment : Fragment() {

    private var _binding: FragmentMypageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMypageBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // api 연결
        val apiService = RetrofitClient.apiservice
        val gson = Gson()

        // token 가져오기
        val globalAccessToken: String = App.prefs.getItem("accessToken","no Token")

        // user정보 가져오기
        val token = "Bearer ${globalAccessToken.replace("\"", "")}"

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val responseData = apiService.myPage(token)
                Log.e("Response", responseData.toString())
                val data = gson.fromJson(responseData.data.toString(), JsonObject::class.java)

                // userRole 저장
                App.prefs.addItem("userRole",data["userRole"].toString().replace("\"", ""))
                val name = data["name"].toString().replace("\"", "")
                val school = data["univ"].toString().replace("\"", "")
                val depart = data["department"].toString().replace("\"", "")
                val point = data["point"].toString().replace("\"", "")

                binding.root.post {
                    binding.userName.text = name
                    binding.userSchool.text = school
                    binding.userDepart.text = depart
                    binding.userPoint.text = point
                }

                // 응답 데이터를 사용하여 작업 수행
            } catch (e: Exception) {
                if (e is retrofit2.HttpException){
                    if (e.code() == 404){
                        val errorBody = e.response()?.errorBody()?.string()
                        val gson = Gson()
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

        // 회원정보 변경 이동
        binding.setting.setOnClickListener{
            activity.let {
                val userR = App.prefs.getItem("userRole","noUserRole")
                if (userR == "UNIV"){
                    val intent = Intent(context, EditUnivActivity::class.java)
                    startActivity(intent)
                } else {
                    val intent = Intent(context, EditHighActivity::class.java)
                    startActivity(intent)
                }

            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}