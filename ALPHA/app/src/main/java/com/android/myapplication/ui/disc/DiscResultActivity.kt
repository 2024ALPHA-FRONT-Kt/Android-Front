package com.android.myapplication.ui.disc

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.os.Environment
import android.util.JsonReader
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.android.myapplication.App
import com.android.myapplication.MainActivity
import com.android.myapplication.api.RetrofitClient
import com.android.myapplication.databinding.ActivityDiscResultBinding
import com.android.myapplication.dto.ResponseObject
import com.android.myapplication.ui.disc.data_class.DiscScore
import com.android.myapplication.ui.disc.data_class.DiscTestResult
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.io.StringReader
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class DiscResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDiscResultBinding
    private lateinit var discScore: DiscScore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiscResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.discTestResultPageBack.setOnClickListener {
            val intent = Intent(this, DiscActivity::class.java)
            startActivity(intent)
        }

        binding.discResultPageCompleteButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.discResultPageShareButton.setOnClickListener {
            val screenshot = takeScreenshotOfView(binding.savingResultImg)
            val imageFile = saveScreenshot(screenshot)
            shareImage(imageFile)
        }

        val apiService = RetrofitClient.apiservice
        val gson = Gson()
        val globalAccessToken: String = App.prefs.getItem("accessToken", "no Token")
        val token = "Bearer ${globalAccessToken.replace("\"", "")}"

        discScore = intent.getParcelableExtra("DISC_SCORE") ?: DiscScore() // 전달받은 DiscScore

        val disc = DiscTestResult(
            dscore = discScore.DScore,
            iscore = discScore.IScore,
            sscore = discScore.SScore,
            cscore = discScore.CScore
        )

        GlobalScope.launch(Dispatchers.IO) {
            try {
                // API 호출 예제
                val responseData = apiService.postDiscTestResult(token, disc)

                // API 응답 데이터 확인
                val jsonData = gson.fromJson(responseData.toString(), JsonObject::class.java)
                Log.e("API Response Data", jsonData.toString())
                Log.e("API Response Data", jsonData["discCode"].toString())

//                val realData = data["discCode"].asJsonObject
//
//                val discType = realData["category"].asString
//                val discTypeEn = realData["key"].asString
//                val discPros = realData["pros"].asString
//                val discEx = realData["ex"].asString
//                val discJob = realData["job"].asString
//                val discProsJob = realData["prosJob"].asString
//
//                binding.discType.text = "$discType - $discTypeEn"
//                binding.discPros.text = discPros
//                binding.discEx.text = discEx
//                binding.discJob.text = discJob
//                binding.discPos.text = discProsJob

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
}