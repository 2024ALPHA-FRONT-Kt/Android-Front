package com.android.myapplication.ui.knowledge_community

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.myapplication.R

class KnowledgePostListFragment : Fragment() {

    companion object {
        fun newInstance() = KnowledgePostListFragment()
    }

    private val viewModel: KnowledgePostListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_knowledge_post_list, container, false)
    }
}