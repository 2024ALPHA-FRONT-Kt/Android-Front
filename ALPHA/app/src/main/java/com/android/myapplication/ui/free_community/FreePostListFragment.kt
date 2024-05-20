package com.example.xptmxmeksid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.myapplication.R
import com.android.myapplication.ui.free_community.FreeList

class FreePostListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_free_post_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val list = ArrayList<FreeList>()
        list.add(FreeList("제목 1", "내용 1"))
        list.add(FreeList("제목 2", "내용 2"))
        list.add(FreeList("제목 3", "내용 3"))

        val recyclerView = view.findViewById<RecyclerView>(R.id.free_list_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = FreePostAdapter(list)
        recyclerView.adapter = adapter
    }
}
