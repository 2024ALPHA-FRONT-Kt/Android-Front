package com.android.myapplication.ui.knowledge_community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.myapplication.databinding.FragmentKnowledgePostListBinding

class KnowledgePostListFragment : Fragment() {

    private var _binding: FragmentKnowledgePostListBinding? = null
    private val binding get() = _binding!!

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

        val list = ArrayList<KnowledgePosts>().apply {
            add(
                KnowledgePosts(
                    "저 학교 어떻게 다녀야 할까요?",
                    "저 생기부 작성하려고 하는데 혹시 스포츠 건강재활학과에서 어떤 것을 강조하는지 혹시 꿀팁 주실 수 있나요 ㅠㅠ 주면 감사랑 치킨",
                    "0000-00-00"
                )
            )
            add(
                KnowledgePosts(
                    "저 학교 어떻게 다녀야 할까요?",
                    "저 생기부 작성하려고 하는데 혹시 스포츠 건강재활학과에서 어떤 것을 강조하는지 혹시 꿀팁 주실 수 있나요 ㅠㅠ 주면 감사랑 치킨",
                    "0000-00-00"
                )
            )
            add(
                KnowledgePosts(
                    "저 학교 어떻게 다녀야 할까요?",
                    "저 생기부 작성하려고 하는데 혹시 스포츠 건강재활학과에서 어떤 것을 강조하는지 혹시 꿀팁 주실 수 있나요 ㅠㅠ 주면 감사랑 치킨",
                    "0000-00-00"
                )
            )
            add(
                KnowledgePosts(
                    "저 학교 어떻게 다녀야 할까요?",
                    "저 생기부 작성하려고 하는데 혹시 스포츠 건강재활학과에서 어떤 것을 강조하는지 혹시 꿀팁 주실 수 있나요 ㅠㅠ 주면 감사랑 치킨",
                    "0000-00-00"
                )
            )
            add(
                KnowledgePosts(
                    "저 학교 어떻게 다녀야 할까요?",
                    "저 생기부 작성하려고 하는데 혹시 스포츠 건강재활학과에서 어떤 것을 강조하는지 혹시 꿀팁 주실 수 있나요 ㅠㅠ 주면 감사랑 치킨",
                    "0000-00-00"
                )
            )
        }

        binding.knowledgeListView.layoutManager = LinearLayoutManager(context)
        val adapter = KnowledgePostsAdapter(list)
        binding.knowledgeListView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}