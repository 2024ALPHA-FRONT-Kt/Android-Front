package com.android.myapplication.ui.free_community

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.myapplication.R

class WriteFreePostFragment : Fragment() {

    companion object {
        fun newInstance() = WriteFreePostFragment()
    }

    private val viewModel: WriteFreePostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_write_free_post, container, false)
    }
}