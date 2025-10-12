package com.example.umc_9th

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import umc.study.umc_8th.R

class DetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = arguments?.getString("title") ?: "제목 없음"
        val artist = arguments?.getString("artist") ?: "가수 없음"

        val albumTitle = view.findViewById<TextView>(R.id.detail_title)
        val albumArtist = view.findViewById<TextView>(R.id.detail_artist)

        albumTitle.text = title
        albumArtist.text = artist
    }
}
