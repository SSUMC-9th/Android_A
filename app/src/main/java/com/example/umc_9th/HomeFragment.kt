package com.example.umc_9th

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import umc.study.umc_8th.R


class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val mainActivity = activity as? MainActivity

        // song1
        val song1Title = view.findViewById<TextView>(R.id.song1_title)
        val song1Artist = view.findViewById<TextView>(R.id.song1_artist)
        val song1Album = view.findViewById<ImageView>(R.id.song1_album)
        val song1Layout = view.findViewById<LinearLayout>(R.id.song1_container)

        song1Layout.setOnClickListener {
            val intent = Intent(requireContext(), SongActivity::class.java)
            intent.putExtra("title", song1Title.text.toString())
            intent.putExtra("artist", song1Artist.text.toString())
            intent.putExtra("albumResId", R.drawable.img_album_exp)
            mainActivity?.songActivityLauncher?.launch(intent)
        }

        // song2
        val song2Title = view.findViewById<TextView>(R.id.song2_title)
        val song2Artist = view.findViewById<TextView>(R.id.song2_artist)
        val song2Album = view.findViewById<ImageView>(R.id.song2_album)
        val song2Layout = view.findViewById<LinearLayout>(R.id.song2_container)

        song2Layout.setOnClickListener {
            val intent = Intent(requireContext(), SongActivity::class.java)
            intent.putExtra("title", song2Title.text.toString())
            intent.putExtra("artist", song2Artist.text.toString())
            intent.putExtra("albumResId", R.drawable.img_album_exp2)
            mainActivity?.songActivityLauncher?.launch(intent)
        }

        // song3
        val song3Title = view.findViewById<TextView>(R.id.song3_title)
        val song3Artist = view.findViewById<TextView>(R.id.song3_artist)
        val song3Album = view.findViewById<ImageView>(R.id.song3_album)
        val song3Layout = view.findViewById<LinearLayout>(R.id.song3_container)

        song3Layout.setOnClickListener {
            val intent = Intent(requireContext(), SongActivity::class.java)
            intent.putExtra("title", song3Title.text.toString())
            intent.putExtra("artist", song3Artist.text.toString())
            intent.putExtra("albumResId", R.drawable.img_album_exp3)
            mainActivity?.songActivityLauncher?.launch(intent)
        }

        return view
    }
}
