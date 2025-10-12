package com.example.umc_9th

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import umc.study.umc_8th.R

class AlbumFragment : Fragment(R.layout.fragment_album) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = arguments?.getString("title") ?: "제목 없음"
        val artist = arguments?.getString("artist") ?: "가수 없음"
        val albumResId = arguments?.getInt("albumResId") ?: R.drawable.img_album_exp3

        val albumCover = view.findViewById<ImageView>(R.id.album_cover)
        val albumTitle = view.findViewById<TextView>(R.id.album_title)
        val albumArtist = view.findViewById<TextView>(R.id.album_artist)

        albumCover.setImageResource(albumResId)
        albumTitle.text = title
        albumArtist.text = artist

        val tabLayout = view.findViewById<TabLayout>(R.id.album_tabLayout)
        val viewPager = view.findViewById<ViewPager2>(R.id.album_viewPager)
        val btnBack = view.findViewById<ImageButton>(R.id.album_btn_back)

        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        val adapter = AlbumPagerAdapter(this, title, artist)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "수록곡"
                1 -> "상세 정보"
                2 -> "영상"
                else -> null
            }
        }.attach()
    }
}
