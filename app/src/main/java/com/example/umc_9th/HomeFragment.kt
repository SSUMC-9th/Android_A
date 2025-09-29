package com.example.umc_9th

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import umc.study.umc_8th.R
import umc.study.umc_8th.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

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
                songs = listOf(
                    BannerSong("Butter", "방탄소년단", R.drawable.img_album_exp),
                    BannerSong("LILAC", "IU", R.drawable.img_album_exp2)
                )
            ),
            BannerData(
                title = "새로운 감성",
                songs = listOf(
                    BannerSong("Next Level", "aespa", R.drawable.img_album_exp3)
                )
            ),
            BannerData(
                title = "추가 배너",
                songs = listOf(
                    BannerSong("Hype Boy", "뉴진스", R.drawable.img_album_exp)
                )
            )

        )

        val adapter = HomeBannerAdapter(banners) { song ->
            val intent = Intent(requireContext(), SongActivity::class.java).apply {
                putExtra("title", song.title)
                putExtra("artist", song.artist)
                putExtra("albumResId", song.albumResId)
            }
            (activity as? MainActivity)?.songActivityLauncher?.launch(intent)
        }

        binding.homeTopBanner.adapter = adapter
        binding.homeBannerIndicator.setViewPager2(binding.homeTopBanner)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
