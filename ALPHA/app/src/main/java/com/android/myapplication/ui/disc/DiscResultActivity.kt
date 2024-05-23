package com.android.myapplication.ui.disc

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.myapplication.App
import com.android.myapplication.api.RetrofitClient
import com.android.myapplication.databinding.ActivityDiscResultBinding
import com.android.myapplication.ui.disc.data_class.DiscScore
import com.android.myapplication.ui.disc.data_class.DiscTestResult
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DiscResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDiscResultBinding
    private lateinit var discScore: DiscScore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiscResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

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
                val discType = data["category"].toString().replace("\"", "")
                val discTypeEn = data["key"].toString().replace("\"", "")
                val discPros = data["pros"].toString().replace("\"", "")
                val discEx = data["ex"].toString().replace("\"", "")
                val discJob = data["job"].toString().replace("\"", "")
                val discProsJob = data["prosJob"].toString().replace("\"", "")

                binding.discType.text = "$discType - $discTypeEn"
                binding.discPros.text = discPros
                binding.discEx.text = discEx
                binding.discJob.text = discJob
                binding.discPos.text = discProsJob
                Log.e("Response", responseData.toString())

            } catch (e: Exception) {
                Log.e("Error", e.message.toString())
            }
        }
    }
}