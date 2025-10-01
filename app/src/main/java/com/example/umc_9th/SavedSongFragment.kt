package com.example.umc_9th

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import umc.study.umc_8th.R

class SavedSongFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_saved_song, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView_saved_songs)

        val savedSongs = listOf(
            SavedSong(R.drawable.img_album_exp, "Butter", "BTS"),
            SavedSong(R.drawable.img_album_exp2, "LILAC", "IU"),
            SavedSong(R.drawable.img_album_lovewinsall, "Love wins all", "IU"),
            SavedSong(R.drawable.img_album_exp3, "Next Level", "aespa")
        )

        val adapter = SavedSongAdapter(
            savedSongs,
            onPlayClick = { song ->
                // TODO: 재생 로직 추가
            },
            onMoreClick = { song ->
                // TODO: 메뉴 로직 추가
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }
}