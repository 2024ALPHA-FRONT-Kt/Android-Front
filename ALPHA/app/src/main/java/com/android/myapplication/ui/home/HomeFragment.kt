package com.android.myapplication.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.android.myapplication.R
import com.android.myapplication.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // card viewpager 기능
        val card = binding.cardList
        card.adapter = CardAdapter(getCardImages())
        card.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        // dday viewpager 기능
        val dday = binding.pagerDday
        dday.adapter = DdayAdapter(getDdayItems())
        dday.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        return root
    }
    private fun getDdayItems(): ArrayList<Int> {
        return arrayListOf<Int>(
            0,1,2
        )
    }
    private fun getCardImages(): ArrayList<Int> {
        return arrayListOf<Int>(
            R.drawable.img_main_bootcamp,
            R.drawable.img_main_community,
            R.drawable.img_main_disc,)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}