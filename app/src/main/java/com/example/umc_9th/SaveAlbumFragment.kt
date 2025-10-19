package com.example.umc_9th

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import umc.study.umc_9th.R
import umc.study.umc_9th.databinding.FragmentSaveAlbumBinding

class SaveAlbumFragment : Fragment() {
    private lateinit var binding: FragmentSaveAlbumBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSaveAlbumBinding.inflate(inflater, container, false)
        val savedAlbumList = mutableListOf (
            LockerAlbumData("Love wins all", "IU","2024.02.20 ㅣ 정규 ㅣ 감성" ,R.drawable.img_album_lovewinsall),
            LockerAlbumData("Drama", "aspea","2023.11.10 ㅣ 정규 ㅣ K-pop" ,R.drawable.img_album_drama),
            LockerAlbumData("Lilac", "IU","2021.03.25 ㅣ 정규 ㅣ R&B" ,R.drawable.img_album_exp2),
            LockerAlbumData("Supernova", "Le seraphim","2024.05.27 ㅣ 정규 ㅣ K-pop" ,R.drawable.img_album_supernova)
        )
        val adapter = LockerAlbumAdapter(savedAlbumList, onVisitClicked = { song ->

        })
        binding.lockerAlbumRC.adapter = adapter
        binding.lockerAlbumRC.layoutManager = LinearLayoutManager(requireContext())
        return binding.root
    }
}