package com.android.myapplication.ui.free_community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.myapplication.databinding.FragmentFreePostListBinding

class FreePostListFragment : Fragment() {

    private var _binding: FragmentFreePostListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFreePostListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val list = ArrayList<FreePosts>()
        list.add(FreePosts("제목 1", "내용 1"))
        list.add(FreePosts("제목 2", "내용 2"))
        list.add(FreePosts("제목 3", "내용 3"))

        binding.freeListView.layoutManager = LinearLayoutManager(context)
        val adapter = FreePostAdapter(list)
        binding.freeListView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}