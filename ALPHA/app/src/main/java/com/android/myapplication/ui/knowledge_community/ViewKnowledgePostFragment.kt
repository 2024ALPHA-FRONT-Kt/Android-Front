package com.android.myapplication.ui.knowledge_community

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.myapplication.R

class ViewKnowledgePostFragment : Fragment() {

    companion object {
        fun newInstance() = ViewKnowledgePostFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_view_knowledge_post, container, false)
    }
}