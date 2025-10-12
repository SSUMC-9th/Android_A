package com.example.umc_9th

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class AlbumPagerAdapter(fragment: Fragment, private val title: String, private val artist: String)
    : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TrackListFragment()
            1 -> DetailFragment().apply {
                arguments = Bundle().apply {
                    putString("title", title)
                    putString("artist", artist)
                }
            }
            2 -> VideoFragment()
            else -> TrackListFragment()
        }
    }
}

