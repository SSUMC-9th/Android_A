package com.example.umc_9th

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import umc.study.umc_9th.databinding.FragmentHomeBannerBinding

class HomeBannerFragment(val imgRes : Int) : Fragment() {
    private lateinit var binding : FragmentHomeBannerBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBannerBinding.inflate(inflater, container, false)
        binding.bannerImage.setImageResource(imgRes)
        return binding.root
    }

}