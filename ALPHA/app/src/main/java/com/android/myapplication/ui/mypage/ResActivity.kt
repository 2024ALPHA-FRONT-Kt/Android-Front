package com.android.myapplication.ui.mypage

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.myapplication.R
import com.android.myapplication.ui.disc.DiscActivity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.myapplication.App
import com.android.myapplication.MainActivity
import com.android.myapplication.api.RetrofitClient
import com.android.myapplication.databinding.ActivityResBinding
import com.android.myapplication.databinding.ActivityStep2HighBinding
import com.android.myapplication.ui.disc.data_class.DiscData
import com.android.myapplication.ui.disc.data_class.DiscTestResponse
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Dictionary
import java.util.Locale

class ResActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_res)
        supportActionBar?.hide()

        // 바인딩
        binding = ActivityResBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.pageBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.resultPageCompleteButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // api 연결
        val apiService = RetrofitClient.apiservice
        val gson = Gson()
        val globalAccessToken: String = App.prefs.getItem("accessToken", "no Token")
        val token = "Bearer ${globalAccessToken.replace("\"", "")}"

        val shareButton: Button = binding.resultPageShareButton
        shareButton.setOnClickListener {
            val screenshot = takeScreenshot()
            val imageUri = saveScreenshot(screenshot)
            shareImage(imageUri)
        }

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val responseData = apiService.getDiscTestResult(token) // API 호출
                val input = responseData.data.toString()
                Log.e("response",input)
                val cleanedInput = input.trim().removeSurrounding("{", "}")
                val sp = cleanedInput.split(", discCode=").map { it.trim() }
                val discC = sp[1]
                val discCode = parseStringToMap(discC)
                Log.e("parse",discCode.toString())

                val discType = discCode["category"]
                val discTypeEn = discCode["key"]
                val discPros = discCode["pros"]
                val discEx = discCode["ex"]
                val discJob = discCode["job"]
                val discProsJob = discCode["prosJob"]

                binding.root.post{
                    binding.discType.text = "$discType - $discTypeEn"
                    binding.discPros.text = discPros
                    binding.discEx.text = discEx
                    binding.discJob.text = discJob
                    binding.discPos.text = discProsJob
                }

            } catch (e: Exception) {
                Log.e("Error", e.message.toString()) // 에러 로그
            }
        }

    }
    private fun takeScreenshot(): Bitmap {
        val rootView = window.decorView.findViewById<View>(android.R.id.content)
        val screenshot = Bitmap.createBitmap(rootView.width, rootView.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(screenshot)
        rootView.draw(canvas)
        return screenshot
    }

    private fun saveScreenshot(screenshot: Bitmap): File {
        val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "disc_result_$timestamp.jpg"
        val image = File(imagesDir, imageFileName)
        var outputStream: OutputStream? = null
        try {
            outputStream = FileOutputStream(image)
            screenshot.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            Toast.makeText(this, "결과 화면이 저장되었습니다.", Toast.LENGTH_SHORT).show()
        } finally {
            outputStream?.close()
        }
        return image
    }

    private fun shareImage(imageUri: File) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "image/*"
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
        startActivity(Intent.createChooser(shareIntent, "결과 공유하기"))
    }
    private fun parseStringToMap(input: String): Map<String, String> {
        // 문자열 양 끝의 중괄호를 제거하고 공백을 제거합니다.
        val cleanedInput = input.trim().removeSurrounding("{", "}")

        // 각 항목을 분리합니다.
        val entries = cleanedInput.split("(?<=\\w)(,\\s)(?=\\w+=)".toRegex())

        // 항목을 key-value 쌍으로 분리하여 Map에 추가합니다.
        val map = mutableMapOf<String, String>()
        for (entry in entries) {
            val keyValue = entry.split("=", limit = 2)
            if (keyValue.size == 2) {
                val key = keyValue[0].trim()
                val value = keyValue[1].trim()
                map[key] = value
            }
        }

        return map
    }
}