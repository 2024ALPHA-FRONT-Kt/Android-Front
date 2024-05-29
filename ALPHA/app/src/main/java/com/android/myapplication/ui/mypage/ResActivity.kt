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
import android.view.View
import android.widget.Toast
import com.android.myapplication.MainActivity
import com.android.myapplication.databinding.ActivityResBinding
import com.android.myapplication.databinding.ActivityStep2HighBinding
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

        binding.pageBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.resultPageCompleteButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val shareButton: Button = binding.resultPageShareButton
        shareButton.setOnClickListener {
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
}