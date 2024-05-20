package com.android.myapplication.ui.free_community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.myapplication.R

class ViewFreePostPlusFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val list = ArrayList<FreeComments>()
        list.add(FreeComments("국민대학교", "uid1", "하"))
        list.add(FreeComments("국민대학교", "uid2", "하"))
        list.add(FreeComments("국민대학교", "uid3", "하"))
        list.add(FreeComments("국민대학교", "uid4", "하"))

        val adapter = FreeCommentAdapter(list)
        val recyclerView = view.findViewById<RecyclerView>(R.id.free_comment_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }
}
