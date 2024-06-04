package com.android.myapplication.ui.mypage

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.android.myapplication.App
import com.android.myapplication.MainActivity
import com.android.myapplication.R
import com.android.myapplication.api.RetrofitClient
import com.android.myapplication.databinding.ActivityResBinding
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
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

        binding.resultPageCompleteButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // api 연결
        val apiService = RetrofitClient.apiservice
        val gson = Gson()
        val globalAccessToken: String = App.prefs.getItem("accessToken", "no Token")
        val token = "Bearer ${globalAccessToken.replace("\"", "")}"

        binding.resultPageShareButton.setOnClickListener {
            val viewToSave: View = binding.savingResultImg

            val bitmap =
                Bitmap.createBitmap(viewToSave.width, viewToSave.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            viewToSave.draw(canvas)

            val timeStamp: String =
                SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "Disc_Result_$timeStamp.jpg"
            val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

            val imageFile = File.createTempFile(fileName, ".jpg", storageDir)

            val outputStream: OutputStream = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()

            Toast.makeText(this, "저장 완료!", Toast.LENGTH_SHORT).show()

            val imageUri = FileProvider.getUriForFile(
                this,
                "com.android.myapplication.fileprovider",
                imageFile
            )

            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, imageUri)
                type = "image/jpeg"
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            startActivity(Intent.createChooser(shareIntent, "이미지 공유하기"))
        }


        GlobalScope.launch(Dispatchers.IO) {
            try {
                val responseData = apiService.getDiscTestResult(token)
                val input = responseData.data.toString()
                Log.e("response", input)
                val cleanedInput = input.trim().removeSurrounding("{", "}")
                val sp = cleanedInput.split(", discCode=").map { it.trim() }
                val discC = sp[1]
                val discCode = parseStringToMap(discC)
                Log.e("parse", discCode.toString())

                val sameUsers = sp[0]
                val discType = discCode["category"]
                val discTypeEn = discCode["key"]
                val discPros = discCode["pros"]
                val discEx = discCode["ex"]
                val discJob = discCode["job"]

                binding.root.post {
                    binding.discType.text = "$discType - $discTypeEn"
                    binding.discPros.text = discPros
                    binding.discEx.text = discEx
                    binding.discJob.text = discJob
                    binding.discSameUsers.text = sameUsers.substring(12 until sameUsers.length - 2)
                }

            } catch (e: Exception) {
                Log.e("Error", e.message.toString()) // 에러 로그
            }
        }

    }

    private fun takeScreenshotOfView(view: View): Bitmap {
        val screenshot = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(screenshot)
        view.draw(canvas)
        return screenshot
    }

    private fun saveScreenshot(screenshot: Bitmap): File {
        val imagesDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "disc_result_$timestamp.jpg"
        val imageFile = File(imagesDir, imageFileName)
        var outputStream: OutputStream? = null
        try {
            outputStream = FileOutputStream(imageFile)
            screenshot.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            Toast.makeText(this, "DISC-T 결과를 공유합니다!.", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            outputStream?.close()
        }
        return imageFile
    }
    private fun shareImage(imageFile: File) {
        val imageUri = FileProvider.getUriForFile(this, "${packageName}.provider", imageFile)
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_STREAM, imageUri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(Intent.createChooser(shareIntent, "DISC 테스트 결과 공유하기"))
    }

    private fun parseStringToMap(input: String): Map<String, String> {
        val cleanedInput = input.trim().removeSurrounding("{", "}")

        val entries = cleanedInput.split("(?<=\\w)(,\\s)(?=\\w+=)".toRegex())

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