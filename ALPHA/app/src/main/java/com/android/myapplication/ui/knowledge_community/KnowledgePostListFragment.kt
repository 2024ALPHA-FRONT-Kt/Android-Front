package com.android.myapplication.ui.knowledge_community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.myapplication.R

class KnowledgePostListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_knowledge_post_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val list = ArrayList<KnowledgePosts>()
        list.add(KnowledgePosts("저 학교 어떻게 다녀야 할까요?", "저 생기부 작성하려고 하는데 혹시 스포츠 건강재활학광서 어떤 것ㅇㄹ 강조하는지 혹시 꿀팁 주실 수 있나요 ㅠㅠ 주면 감사랑 치킨", "0000-00-00"))
        list.add(KnowledgePosts("저 학교 어떻게 다녀야 할까요?", "저 생기부 작성하려고 하는데 혹시 스포츠 건강재활학광서 어떤 것ㅇㄹ 강조하는지 혹시 꿀팁 주실 수 있나요 ㅠㅠ 주면 감사랑 치킨", "0000-00-00"))
        list.add(KnowledgePosts("저 학교 어떻게 다녀야 할까요?", "저 생기부 작성하려고 하는데 혹시 스포츠 건강재활학광서 어떤 것ㅇㄹ 강조하는지 혹시 꿀팁 주실 수 있나요 ㅠㅠ 주면 감사랑 치킨", "0000-00-00"))
        list.add(KnowledgePosts("저 학교 어떻게 다녀야 할까요?", "저 생기부 작성하려고 하는데 혹시 스포츠 건강재활학광서 어떤 것ㅇㄹ 강조하는지 혹시 꿀팁 주실 수 있나요 ㅠㅠ 주면 감사랑 치킨", "0000-00-00"))
        list.add(KnowledgePosts("저 학교 어떻게 다녀야 할까요?", "저 생기부 작성하려고 하는데 혹시 스포츠 건강재활학광서 어떤 것ㅇㄹ 강조하는지 혹시 꿀팁 주실 수 있나요 ㅠㅠ 주면 감사랑 치킨", "0000-00-00"))

        val recyclerView = view.findViewById<RecyclerView>(R.id.knowledge_list_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = KnowledgePostsAdapter(list)
        recyclerView.adapter = adapter
    }
}