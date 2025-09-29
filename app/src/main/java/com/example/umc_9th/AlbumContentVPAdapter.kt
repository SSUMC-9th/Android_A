package com.example.umc_9th

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class AlbumContentVPAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> AlbumSongFragment()
            1 -> DetailFragment()
            2 -> VideoFragment()
            else -> AlbumSongFragment()
        }
    }

    override fun getItemCount(): Int {
        return 3
    }

}