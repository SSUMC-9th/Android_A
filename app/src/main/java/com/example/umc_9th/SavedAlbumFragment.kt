package com.example.umc_9th

import umc.study.umc_8th.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import umc.study.umc_8th.databinding.FragmentSavedAlbumBinding

class SavedAlbumFragment : Fragment() {
    lateinit var binding: FragmentSavedAlbumBinding

    private var savedAlbumDatas = ArrayList<SavedAlbum>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedAlbumBinding.inflate(inflater, container, false)

        savedAlbumDatas.apply{
            add(SavedAlbum("LILAC", "아이유(IU)", "2021.03.25 | 정규 | 댄스 팝", R.drawable.img_album_exp2))
            add(SavedAlbum("DRAMA", "aespa", "2023.11.10 | 싱글/EP | 가요 댄스", R.drawable.img_album_drama))
        }

        val savedAlbumRVAdapter = SavedAlbumRVAdapter(savedAlbumDatas)
        binding.lockerSavedSongsRv.adapter = savedAlbumRVAdapter
        binding.lockerSavedSongsRv.layoutManager=
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        return binding.root
    }
}

