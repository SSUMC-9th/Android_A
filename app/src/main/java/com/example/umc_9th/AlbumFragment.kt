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
import com.example.umc_9th.data.firebase.FirebaseManager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import umc.study.umc_8th.R

class AlbumFragment : Fragment(R.layout.fragment_album) {
    private lateinit var firebaseManager: FirebaseManager
    private var isLiked = false
    private var albumId = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseManager = FirebaseManager.getInstance()

        val title = arguments?.getString("title") ?: "제목 없음"
        val artist = arguments?.getString("artist") ?: "가수 없음"
        val albumResId = arguments?.getInt("albumResId") ?: R.drawable.img_album_exp3

        val albumCover = view.findViewById<ImageView>(R.id.album_cover)
        val albumTitle = view.findViewById<TextView>(R.id.album_title)
        val albumArtist = view.findViewById<TextView>(R.id.album_artist)
        val btnLike = view.findViewById<ImageButton>(R.id.album_btn_like)

        albumCover.setImageResource(albumResId)
        albumTitle.text = title
        albumArtist.text = artist

        // 🔥 좋아요 상태 확인
        firebaseManager.checkIfAlbumLiked(albumId) { liked ->
            activity?.runOnUiThread {
                isLiked = liked
                btnLike.setImageResource(
                    if (liked) R.drawable.ic_my_like_on
                    else R.drawable.ic_my_like_off
                )
            }
        }

        // 🔥 좋아요 버튼 클릭
        btnLike.setOnClickListener {
            val album = Album(albumId, title, artist, albumResId)

            if (isLiked) {
                firebaseManager.removeLikedAlbum(albumId,
                    onSuccess = {
                        isLiked = false
                        btnLike.setImageResource(R.drawable.ic_my_like_off)
                    },
                    onFailure = {}
                )
            } else {
                firebaseManager.addLikedAlbum(album,
                    onSuccess = {
                        isLiked = true
                        btnLike.setImageResource(R.drawable.ic_my_like_on)
                    },
                    onFailure = {}
                )
            }
        }

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
