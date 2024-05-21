package com.android.myapplication.ui.mypage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.myapplication.App
import com.android.myapplication.api.RetrofitClient
import com.android.myapplication.databinding.FragmentMypageBinding
import com.android.myapplication.dto.ExceptionDto
import com.android.myapplication.ui.user.EditUnivActivity
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MyPageFragment : Fragment() {

    private var _binding: FragmentMypageBinding? = null
    private val binding get() = _binding!!
    var userName = ""
    var userSchool = ""
    var userDepart = ""
    var userPoint = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMypageBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // 회원정보 변경 이동
        binding.setting.setOnClickListener{
            activity.let {
                val intent = Intent(context, EditUnivActivity::class.java)
                startActivity(intent)
            }
        }

        var userName = ""
        var userSchool = ""
        var userDepart = ""
        var userPoint = ""


        // api 연결
        val apiService = RetrofitClient.apiservice
        val gson = Gson()

        // token 가져오기
        val globalAccessToken: String = App.prefs.getToken("accessToken","no Token")

        // user정보 가져오기
        val token = "Bearer ${globalAccessToken.replace("\"", "")}"

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val responseData = apiService.myPage(token)
                Log.e("Response", responseData.toString())
                val data = gson.fromJson(responseData.data.toString(), JsonObject::class.java)

//                userName = data["name"].toString()
//                userSchool = data["univ"].toString()
//                userDepart = data["department"].toString()
//                userPoint = data["point"].toString()

                binding.userName.text = data["name"].toString().replace("\"", "")
                binding.userSchool.text = data["univ"].toString().replace("\"", "")
                binding.userDepart.text = data["department"].toString().replace("\"", "")
                binding.userPoint.text = data["point"].toString().replace("\"", "")

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

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}