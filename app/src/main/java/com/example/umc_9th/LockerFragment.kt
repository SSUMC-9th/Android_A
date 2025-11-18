package com.example.umc_9th

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import umc.study.umc_8th.R

class LockerFragment : Fragment(R.layout.fragment_locker) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabLayout = view.findViewById<TabLayout>(R.id.locker_tabLayout)
        val viewPager = view.findViewById<ViewPager2>(R.id.locker_viewPager)

        val adapter = LockerPagerAdapter(this)
        viewPager.adapter = adapter
        val loginBtn = view.findViewById<TextView>(R.id.locker_login)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "저장한 곡"
                1 -> tab.text = "음악파일"
                2 -> tab.text = "저장앨범"
            }
        }.attach()

        loginBtn.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
