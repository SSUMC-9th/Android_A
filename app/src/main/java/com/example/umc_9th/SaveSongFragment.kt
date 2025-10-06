package com.example.umc_9th

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import umc.study.umc_9th.R
import umc.study.umc_9th.databinding.FragmentSaveSongBinding

class SaveSongFragment : Fragment() {
    private lateinit var binding: FragmentSaveSongBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSaveSongBinding.inflate(inflater, container, false)
        val savedSongList = mutableListOf (
            LockerSongData("Love wins all", "IU", R.drawable.img_album_lovewinsall),
            LockerSongData("Drama", "aespa", R.drawable.img_album_drama),
            LockerSongData("Lilac", "IU", R.drawable.img_album_exp2),
            LockerSongData("Supernova", "Le Serrapim", R.drawable.img_album_supernova)
        )
        val adapter = LockerSongAdapter(savedSongList, onVisitClicked = {song ->

        })
        binding.lockerSongRC.adapter = adapter
        binding.lockerSongRC.layoutManager = LinearLayoutManager(requireContext())
        return binding.root
    }
}