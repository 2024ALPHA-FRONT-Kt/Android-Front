package com.android.myapplication.ui.knowledge_community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.myapplication.databinding.FragmentViewKnowledgePostBinding

class ViewKnowledgePostFragment : Fragment() {

    private var _binding: FragmentViewKnowledgePostBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = ViewKnowledgePostFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewKnowledgePostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

