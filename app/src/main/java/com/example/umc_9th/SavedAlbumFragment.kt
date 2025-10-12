package com.example.umc_9th

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import umc.study.umc_8th.R


class SavedAlbumFragment : Fragment() {
    private var isPlayOn = false;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_saved_album, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView_saved_albums)

        val savedAlbums = mutableListOf(
            SavedAlbum(R.drawable.img_album_exp5, "BAAM", "모모랜드"),
            SavedAlbum(R.drawable.img_album_exp3, "Next Level", "aespa"),
            SavedAlbum(R.drawable.img_album_exp6, "Weekend", "태연")

        )

        val adapter = SavedAlbumAdapter(savedAlbums)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }
}