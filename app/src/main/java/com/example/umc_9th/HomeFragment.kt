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
                    BannerSong("supernova", "aespa", R.drawable.img_album_supernova),
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

        binding.recyclerViewAlbum1Container.setOnClickListener {
            val intent = Intent(requireContext(), SongActivity::class.java).apply {
                putExtra("title", binding.recyclerViewAlbum1Title.text.toString())
                putExtra("artist", binding.recyclerViewAlbum1Artist.text.toString())
                putExtra("albumResId", R.drawable.img_album_exp3)
            }
            (activity as? MainActivity)?.songActivityLauncher?.launch(intent)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
