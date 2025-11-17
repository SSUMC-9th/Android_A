package com.example.umc_9th

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import umc.study.umc_9th.R
import umc.study.umc_9th.databinding.FragmentAlbumBinding

class AlbumFragment : Fragment() {
    private lateinit var binding : FragmentAlbumBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(layoutInflater)
        arguments?.let {
            val title = it.getString("title")
            val singer = it.getString("singer")
            binding.albumTitle.text = title
            binding.albumSinger.text = singer
        }
        binding.albumBackButton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, HomeFragment())
                .commit()
        }
        val contentAdapter = AlbumContentVPAdapter(this)
        binding.albumContentVP.adapter = contentAdapter
        binding.albumContentVP.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        TabLayoutMediator(binding.albumContentTB, binding.albumContentVP) {tab, position ->
            tab.text = when(position) {
                0 -> "수록곡"
                1 -> "상세정보"
                2 -> "영상"
                else -> ""
            }
        }.attach()

        return binding.root
    }

}