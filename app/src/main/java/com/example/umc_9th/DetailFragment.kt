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
        val view = inflater.inflate(R.layout.fragment_detail, container, false)

        val albumName = view.findViewById<TextView>(R.id.album_name)
        val composerName = view.findViewById<TextView>(R.id.composer_name)

        albumName.text = "앨범 이름 : My Album"
        composerName.text = "작곡가 : 홍길동"

        return view
    }
}
