package com.example.umc_9th

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import umc.study.umc_9th.databinding.FragmentLockerBinding

class LockerFragment : Fragment() {
    private lateinit var binding : FragmentLockerBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLockerBinding.inflate(layoutInflater)
        val contentAdapter = LockerContentVPAdapter(this)
        binding.lockerContentVP.adapter = contentAdapter
        binding.lockerContentVP.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        TabLayoutMediator(binding.lockerContentTB, binding.lockerContentVP) {tab, position ->
            tab.text = when(position) {
                0 -> "저장한 곡"
                1 -> "음악파일"
                2 -> "저장앨범"
                else -> ""
            }
        }.attach()
        return binding.root
    }

}