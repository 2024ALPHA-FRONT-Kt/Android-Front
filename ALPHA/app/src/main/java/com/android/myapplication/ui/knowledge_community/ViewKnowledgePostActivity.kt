package com.android.myapplication.ui.knowledge_community

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import com.android.myapplication.App
import com.android.myapplication.R
import com.android.myapplication.api.RetrofitClient
import com.android.myapplication.databinding.ActivityViewKnowledgePostBinding
import com.android.myapplication.ui.knowledge_community.data_class.ViewingKnowledge
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewKnowledgePostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewKnowledgePostBinding
    private val apiService = RetrofitClient.apiservice
    private val gson = Gson()
    private val globalAccessToken: String = App.prefs.getItem("accessToken", "no Token")
    private val token = "Bearer ${globalAccessToken.replace("\"", "")}"
    private val id: String = App.prefs.getItem("userId", "noID")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewKnowledgePostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        fetchKnowledgePostDetail()
        binding.viewKnowledgePostMenu.setOnClickListener {
            popup(binding.viewKnowledgePostMenu)
        }
    }

    private fun fetchKnowledgePostDetail() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val responseData = apiService.knowledgePostDetail(token, "556ba748-d8b4-4258-8333-4497697a1a67")
                Log.d("dmddo", responseData.toString())
                val jsonObject = gson.fromJson(responseData.data.toString(), JsonObject::class.java)
                val data = gson.fromJson(jsonObject, ViewingKnowledge::class.java)
                val userEmail = data.email.split("@")[0]
                Log.d("dmddo", data.toString())

                withContext(Dispatchers.Main) {
                    binding.viewKnowledgePostUserId.text = "${data.univ} $userEmail"
                    binding.viewKnowledgePostTitle.text = data.title
                    binding.viewKnowledgePostContent.text = data.content
                }
            } catch (e: Exception) {
                Log.e("ViewKnowledgePostActivity", e.toString())
            }
        }
    }

    private fun popup(v: View) {
        val popup = PopupMenu(this, v)
        popup.menuInflater.inflate(R.menu.k_plus_q_menu, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            clickMenu(item)
        }
        popup.show()
    }

    private fun clickMenu(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.k_plus_q_menu_3 -> {
                GlobalScope.launch(Dispatchers.IO) {
                    try {
                        val responseData = apiService.knowledgePostDetail(token, "556ba748-d8b4-4258-8333-4497697a1a67")
                        val jsonObject = gson.fromJson(responseData.data.toString(), JsonObject::class.java)
                        val data = gson.fromJson(jsonObject, ViewingKnowledge::class.java)
                        val editTitleDraft = data.title
                        val editContentDraft = data.content

                        withContext(Dispatchers.Main) {
                            val intent = Intent(this@ViewKnowledgePostActivity, WriteKnowledgePostActivity::class.java).apply {
                                putExtra("editTitleDraft", editTitleDraft)
                                putExtra("editContentDraft", editContentDraft)
                            }
                            startActivity(intent)
                        }
                    } catch (e: Exception) {
                        Log.e("ViewKnowledgePostActivity", e.toString())
                    }
                }
                true
            }
            else -> false
        }
    }
}
