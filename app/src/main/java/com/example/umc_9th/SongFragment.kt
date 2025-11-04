package com.example.umc_9th

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import umc.study.umc_8th.R
import umc.study.umc_8th.databinding.FragmentSongBinding

class SongFragment : Fragment() {
    lateinit var binding: FragmentSongBinding
    private var songsDatas = ArrayList<Songs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSongBinding.inflate(inflater, container, false)

        songsDatas.apply {
            add(Songs(1, "LILAC", "아이유 (IU)"))
            add(Songs(2, "flu", "아이유 (IU)"))
            add(Songs(3, "Coin", "아이유 (IU)"))
            add(Songs(4, "봄 안녕 봄", "아이유 (IU)"))
            add(Songs(5, "Celebrity", "아이유 (IU)"))
            add(Songs(6, "돌림노래", "아이유 (IU)"))
        }

        val songsRVAdapter = SongsRVAdapter(songsDatas)
        binding.songsRv.adapter = songsRVAdapter
        binding.songsRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)


        return binding.root
    }
}
