package com.android.myapplication.ui.disc

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.myapplication.App
import com.android.myapplication.MainActivity
import com.android.myapplication.api.RetrofitClient
import com.android.myapplication.databinding.ActivityDiscResultBinding
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
            val screenshot = takeScreenshot()
            val imageUri = saveScreenshot(screenshot)
            shareImage(imageUri)
        }


        val apiService = RetrofitClient.apiservice
        val gson = Gson()
        val globalAccessToken: String = App.prefs.getItem("accessToken", "no Token")
        val token = "Bearer ${globalAccessToken.replace("\"", "")}"

        val discScore = intent.getParcelableExtra<DiscScore>("DISC_SCORE") ?: DiscScore()

        val disc = DiscTestResult(
            dscore = discScore.DScore,
            iscore = discScore.IScore,
            sscore = discScore.SScore,
            cscore = discScore.CScore
        )

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val responseData = apiService.postDiscTestResult(token, disc)
                val data = gson.fromJson(responseData.data.toString(), JsonObject::class.java)
                val realData = data["discCode"].toString()

                Log.d("datadatadata", data.toString())
                Log.d("realrealreal", realData)

//                val discType = realData["category"].toString().replace("\"", "")
//                val discTypeEn = realData["key"].toString().replace("\"", "")
//                val discPros = realData["pros"].toString().replace("\"", "")
//                val discEx = realData["ex"].toString().replace("\"", "")
//                val discJob = realData["job"].toString().replace("\"", "")
//                val discProsJob = realData["prosJob"].toString().replace("\"", "")
//
//                binding.discType.text = "$discType - $discTypeEn"
//                binding.discPros.text = discPros
//                binding.discEx.text = discEx
//                binding.discJob.text = discJob
//                binding.discPos.text = discProsJob
                Log.e("Response", responseData.toString())


            } catch (e: Exception) {
                Log.e("Error", e.message.toString())
            }
        }
    }

    private fun takeScreenshot(): Bitmap {
        val rootView = window.decorView.findViewById<View>(android.R.id.content)
        val screenshot =
            Bitmap.createBitmap(rootView.width, rootView.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(screenshot)
        rootView.draw(canvas)
        return screenshot
    }

    private fun saveScreenshot(screenshot: Bitmap): File {
        val imagesDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
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
}
//package com.android.myapplication.ui.disc
//
//import android.os.Bundle
//import android.util.Log
//import androidx.appcompat.app.AppCompatActivity
//import com.android.myapplication.App
//import com.android.myapplication.api.RetrofitClient
//import com.android.myapplication.databinding.ActivityDiscResultBinding
//import com.android.myapplication.ui.disc.data_class.DiscScore
//import com.android.myapplication.ui.disc.data_class.DiscTestResult
//import com.google.gson.Gson
//import com.google.gson.JsonObject
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.GlobalScope
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//import java.lang.Exception
//
//class DiscResultActivity : AppCompatActivity() {
//
//    private lateinit var binding: ActivityDiscResultBinding
//    private lateinit var discScore: DiscScore
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityDiscResultBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        supportActionBar?.hide()
//
//        val apiService = RetrofitClient.apiservice
//        val gson = Gson()
//        val globalAccessToken: String = App.prefs.getItem("accessToken", "no Token")
//        val token = "Bearer ${globalAccessToken.replace("\"", "")}"
//
//        val discScore = intent.getParcelableExtra<DiscScore>("DISC_SCORE") ?: DiscScore()
//
//        val disc = DiscTestResult(
//            dscore = discScore.DScore,
//            iscore = discScore.IScore,
//            sscore = discScore.SScore,
//            cscore = discScore.CScore
//        )
//
//        GlobalScope.launch(Dispatchers.IO) {
//            try {
//                val responseData = apiService.postDiscTestResult(token, disc)
//                val data = gson.fromJson(responseData.data.toString(), JsonObject::class.java)
//
//                val discType = data["category"].toString().replace("\"", "")
//                val discTypeEn = data["key"].toString().replace("\"", "")
//                val discPros = data["pros"].toString().replace("\"", "")
//                val discEx = data["ex"].toString().replace("\"", "")
//                val discJob = data["job"].toString().replace("\"", "")
//                val discProsJob = data["prosJob"].toString().replace("\"", "")
//
//                withContext(Dispatchers.Main) {
//                    binding.discType.text = "$discType - $discTypeEn"
//                    binding.discPros.text = discPros
//                    binding.discEx.text = discEx
//                    binding.discJob.text = discJob
//                    binding.discPos.text = discProsJob
//                }
//                Log.e("Response", responseData.toString())
//
//            } catch (e: Exception) {
//                Log.e("Error", e.message.toString())
//            }
//        }
//    }
//}