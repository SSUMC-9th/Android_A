package com.example.umc_9th

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import umc.study.umc_8th.R
import umc.study.umc_8th.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val slideHandler = Handler(Looper.getMainLooper())
    private var slideRunnable: Runnable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val banners = listOf(
            BannerData(
                title = "달빛의 감성 산책",
                content = "총 10곡 2025.03.30",
                songs = listOf(
                    BannerSong("Butter", "BTS", R.drawable.img_album_exp),
                    BannerSong("LILAC", "IU", R.drawable.img_album_exp2)
                )
            ),
            BannerData(
                title = "힐링을 위한 감성",
                content = "총 7곡 2025.03.30",
                songs = listOf(
                    BannerSong("Weekend", "태연", R.drawable.img_album_exp6),
                    BannerSong("Love wins all", "IU", R.drawable.img_album_lovewinsall)
                )
            ),
            BannerData(
                title = "운동할 때 듣는 음악",
                content = "총 8곡 2025.03.30",
                songs = listOf(
                    BannerSong("해야(Heya)", "IVE", R.drawable.img_album_heya),
                    BannerSong("supernova", "aespa", R.drawable.img_album_supernova)
                )
            )
        )

        val bannerAdapter = HomeBannerAdapter(banners) { song ->
            val albumFragment = AlbumFragment().apply {
                arguments = Bundle().apply {
                    putString("title", song.title)
                    putString("artist", song.artist)
                    putInt("albumResId", song.albumResId)
                }
            }

            parentFragmentManager.beginTransaction()
                .replace(R.id.container, albumFragment)
                .addToBackStack(null)
                .commit()
        }
        binding.homeTopBanner.adapter = bannerAdapter
        binding.homeBannerIndicator.setViewPager2(binding.homeTopBanner)

        slideRunnable = object : Runnable {
            override fun run() {
                if (_binding == null) return  // 🔥 null 체크!

                val itemCount = bannerAdapter.itemCount
                if (itemCount > 0) {
                    val nextItem = (binding.homeTopBanner.currentItem + 1) % itemCount
                    binding.homeTopBanner.setCurrentItem(nextItem, true)
                }
                slideHandler.postDelayed(this, 3000)
            }
        }

        slideRunnable?.let { slideHandler.postDelayed(it, 3000) }

        binding.homeTopBanner.registerOnPageChangeCallback(
            object : androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    slideRunnable?.let {
                        slideHandler.removeCallbacks(it)
                        slideHandler.postDelayed(it, 3000)
                    }
                }
            })

        val albums = listOf(
            Album(1, "Next Level", "aespa", R.drawable.img_album_exp3),
            Album(2, "해야 (Heya)", "IVE", R.drawable.img_album_heya),
            Album(3, "Supernova", "aespa", R.drawable.img_album_supernova)
        )

        val recyclerViewAdapter = HomeAlbumAdapter(albums,
            onAlbumClick = { album ->
                val albumFragment = AlbumFragment().apply {
                    arguments = Bundle().apply {
                        putInt("albumId", album.id)
                        putString("title", album.title)
                        putString("artist", album.artist)
                        putInt("albumResId", album.albumResId)
                    }
                }

                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, albumFragment)
                    .addToBackStack(null)
                    .commit()
            },
            onPlayClick = { album ->
                (activity as? MainActivity)?.updateMiniPlayer(
                    album.title,
                    album.artist,
                    album.albumResId
                )
            }
        )

        binding.recyclerViewAlbum.adapter = recyclerViewAdapter
        binding.recyclerViewAlbum.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        slideRunnable?.let { slideHandler.removeCallbacks(it) }
        slideRunnable = null
        _binding = null
    }
}