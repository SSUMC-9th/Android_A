package com.example.umc_9th

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class LockerPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SavedSongFragment()
            1 -> MusicFileFragment()
            2 -> AlbumFragment()
            else -> SavedSongFragment()
        }
    }
}
