package com.example.umc_9th

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import umc.study.umc_9th.R
import umc.study.umc_9th.databinding.FragmentSongFileBinding

class SongFileFragment : Fragment() {
    private lateinit var binding : FragmentSongFileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSongFileBinding.inflate(inflater, container, false)
        return binding.root
    }
}