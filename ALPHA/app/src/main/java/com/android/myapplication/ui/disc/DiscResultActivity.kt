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
import androidx.core.content.FileProvider
import com.android.myapplication.App
import com.android.myapplication.MainActivity
import com.android.myapplication.api.RetrofitClient
import com.android.myapplication.databinding.ActivityDiscResultBinding
import com.android.myapplication.ui.disc.data_class.DiscScore
import com.android.myapplication.ui.disc.data_class.DiscTestResult
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


        val apiService = RetrofitClient.apiservice
        val gson = Gson()
        val globalAccessToken: String = App.prefs.getItem("accessToken", "no Token")
        val token = "Bearer ${globalAccessToken.replace("\"", "")}"

        discScore = intent.getParcelableExtra("DISC_SCORE") ?: DiscScore()

        val disc = DiscTestResult(
            dscore = discScore.DScore,
            iscore = discScore.IScore,
            sscore = discScore.SScore,
            cscore = discScore.CScore
        )

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val responseData = apiService.postDiscTestResult(token, disc)
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
                Log.e("Error", e.message.toString())
            }
        }
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