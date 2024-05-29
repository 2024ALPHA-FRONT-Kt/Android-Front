package com.android.myapplication.ui.bootcamp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.myapplication.R
import com.android.myapplication.databinding.FragmentInfo1Tab2Binding
import com.android.myapplication.databinding.FragmentInfo1Tab3Binding

class Info1Tab2Fragment : Fragment() {
    private var _binding: FragmentInfo1Tab2Binding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInfo1Tab2Binding.inflate(inflater, container, false)
        val root: View = binding.root





        return root
    }
    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }
}