package com.example.umc_9th

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import umc.study.umc_8th.databinding.FragmentBannerBinding

class BannerFragment() : Fragment() {
    lateinit var binding : FragmentBannerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBannerBinding.inflate(inflater, container, false)
        val imgRes = arguments?.getInt("imgRes")

        if (imgRes != null) {
            binding.bannerImageIv.setImageResource(imgRes)
        }

        return binding.root
    }
}