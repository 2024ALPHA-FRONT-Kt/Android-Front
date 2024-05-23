package com.android.myapplication.ui.disc

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.android.myapplication.App
import com.android.myapplication.MainActivity
import com.android.myapplication.api.RetrofitClient
import com.android.myapplication.databinding.ActivityDiscResultBinding
import com.android.myapplication.dto.DiscResultResponse
import com.android.myapplication.ui.disc.data_class.DiscScore
import com.android.myapplication.ui.disc.data_class.DiscTestResult
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
    private val apiService = RetrofitClient.apiservice

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiscResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val discScore = intent.getParcelableExtra<DiscScore>("DISC_SCORE")
        if (discScore != null) {
            val discTestResult = DiscTestResult(
                dscore = discScore.DScore,
                iscore = discScore.IScore,
                sscore = discScore.SScore,
                cscore = discScore.CScore
            )
            saveDiscResult(discTestResult)
        } else {
            Toast.makeText(this, "DISC 결과를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show()
        }

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
    }

    private fun takeScreenshot(): Bitmap {
        val rootView = window.decorView.findViewById<View>(android.R.id.content)
        val screenshot = Bitmap.createBitmap(rootView.width, rootView.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(screenshot)
        rootView.draw(canvas)
        return screenshot
    }

    private fun saveScreenshot(screenshot: Bitmap): File {
        val imagesDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
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
        val uri = FileProvider.getUriForFile(this, "${packageName}.provider", imageUri)
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(Intent.createChooser(shareIntent, "결과 공유하기"))
    }

    private fun saveDiscResult(score: DiscTestResult) {
        val globalAccessToken: String = App.prefs.getItem("accessToken","no Token")
        val token = "Bearer ${globalAccessToken.replace("\"", "")}"

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = apiService.saveDiscTestResult(token, score)
                getDiscResult(token)
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this@DiscResultActivity, "DISC 결과를 저장하는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getDiscResult(token: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = apiService.getDiscResult(token)
                runOnUiThread {
                    handleDiscResult(response)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this@DiscResultActivity, "DISC 결과를 가져오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun handleDiscResult(response: DiscResultResponse) {
        val discResultData = response.data
        val discCodeData = discResultData.discCode

        binding.discType.text = discCodeData.category
        binding.discPros.text = discCodeData.pros
        binding.discEx.text = discCodeData.ex
        binding.discPos.text = discCodeData.prosJob
        binding.discJob.text = discCodeData.job
    }

}