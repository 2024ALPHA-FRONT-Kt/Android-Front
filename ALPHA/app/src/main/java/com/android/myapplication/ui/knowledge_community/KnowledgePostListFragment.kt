package com.android.myapplication.ui.knowledge_community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.myapplication.App
import com.android.myapplication.R
import com.android.myapplication.api.RetrofitClient
import com.android.myapplication.databinding.FragmentKnowledgePostListBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class KnowledgePostListFragment : Fragment() {

    private var _binding: FragmentKnowledgePostListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: KnowledgePostsAdapter
    private var currentPage = 0

    private val apiService = RetrofitClient.apiservice
    private val gson = Gson()
    private val globalAccessToken: String = App.prefs.getItem("accessToken", "no Token")
    private val token = "Bearer ${globalAccessToken.replace("\"", "")}"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKnowledgePostListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val initialList = ArrayList<KnowledgePosts>()
        adapter = KnowledgePostsAdapter(initialList) { item ->
            val fragment = ViewKnowledgePostFragment.newInstance(item)
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.navigation_view_knowledge_post, fragment)
                ?.addToBackStack(null)
                ?.commit()
        }

        binding.knowledgeListView.layoutManager = LinearLayoutManager(context)
        binding.knowledgeListView.adapter = adapter

        binding.knowledgeListView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                if (layoutManager.findLastCompletelyVisibleItemPosition() == adapter.itemCount - 1) {
                    currentPage++
                    loadPosts(currentPage)
                }
            }
        })

        loadPosts(currentPage)
    }

    private fun loadPosts(page: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.knowLedgeLists(
                    token,
                    "QNA",
                    page
                )
                val dataJson = gson.toJson(response.data)
                val type = object : TypeToken<List<KnowledgePosts>>() {}.type
                val newPosts: List<KnowledgePosts> = gson.fromJson(dataJson, type)
                CoroutineScope(Dispatchers.Main).launch {
                    adapter.addPosts(newPosts)
                }
            } catch (e: Exception) {
                // todo
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}