package com.example.umc_9th

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import umc.study.umc_9th.R
import umc.study.umc_9th.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)
        binding.MusicImage1.setOnClickListener {
            val bundle = Bundle().apply {
                putString("title",binding.albumTitleA.text.toString())
                putString("singer",binding.albumSingerA.text.toString())
            }
            AlbumFragment().arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, AlbumFragment())
                .addToBackStack(null)
                .commit()
        }

        val bannerAdapter = BannerMusicVPAdapter(this)
        bannerAdapter.addFragment(HomeBannerFragment(R.drawable.img_first_album_default))
        bannerAdapter.addFragment(HomeBannerFragment(R.drawable.img_second_album_default))
        binding.homeMusicBanner.adapter = bannerAdapter
        binding.homeMusicBanner.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        return binding.root

    }



}