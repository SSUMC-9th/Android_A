package com.example.umc_9th

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import androidx.recyclerview.widget.LinearLayoutManager
import umc.study.umc_8th.R
import umc.study.umc_8th.databinding.FragmentSavedSongBinding

class SavedSongFragment : Fragment() {
    lateinit var binding: FragmentSavedSongBinding
    private var savedSongDatas = ArrayList<SavedSong>()
    private var gson: Gson = Gson()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedSongBinding.inflate(inflater, container, false)

        savedSongDatas.apply {
            add(SavedSong("LILAC", "아이유 (IU)", R.drawable.img_album_exp2))
            add(SavedSong("Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp))
            add(SavedSong("Next Level", "aespa", R.drawable.img_album_exp3))
            add(SavedSong("Drama", "aespa", R.drawable.img_album_drama))
            add(SavedSong("Boy with Luv", "방탄소년단 (BTS)", R.drawable.img_album_exp4))
            add(SavedSong("SUPERNOVA", "aespa", R.drawable.img_album_supernova))
        }

        val savedSongRVAdapter = SavedSongRVAdapter(savedSongDatas)
        binding.lockerSavedSongsRv.adapter = savedSongRVAdapter
        binding.lockerSavedSongsRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        return binding.root
    }
}
