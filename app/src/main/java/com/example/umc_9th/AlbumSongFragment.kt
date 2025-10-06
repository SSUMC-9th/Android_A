package com.example.umc_9th

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import umc.study.umc_9th.R
import umc.study.umc_9th.databinding.FragmentAlbumSongBinding
import umc.study.umc_9th.databinding.FragmentHomeBinding

class AlbumSongFragment : Fragment() {

    private lateinit var binding : FragmentAlbumSongBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumSongBinding.inflate(inflater, container, false)
        val albumSongList = mutableListOf(
            AlbumSongData("Love wins all", "IU", 1),
            AlbumSongData("Lilac", "IU", 2),
            AlbumSongData("내 손을 잡아", "IU", 3),
            AlbumSongData("Coin", "IU", 4),
            AlbumSongData("Palette", "IU", 5),
            AlbumSongData("분홍신", "IU", 6),

        )
        val adapter = AlbumSongAdapter(albumSongList, onVisitClicked = { album ->

        })
        binding.albumSongRV.adapter = adapter
        binding.albumSongRV.layoutManager= LinearLayoutManager(requireContext())
        return binding.root
    }
}