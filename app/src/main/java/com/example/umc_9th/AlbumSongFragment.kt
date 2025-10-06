package com.example.umc_9th

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import umc.study.umc_9th.R
import umc.study.umc_9th.databinding.FragmentAlbumSongBinding

class AlbumSongFragment : Fragment() {

    private lateinit var binding : FragmentAlbumSongBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumSongBinding.inflate(inflater, container, false)
        return binding.root
    }
}