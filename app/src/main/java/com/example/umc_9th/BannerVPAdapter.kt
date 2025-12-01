package com.example.umc_9th

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class BannerVPAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private val fragmentList : ArrayList<Fragment> = ArrayList()

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]

    fun addFragment(imgRes: Int) {
        val fragment = BannerFragment() // 기본 생성자로 생성
        val bundle = Bundle()
        bundle.putInt("imgRes", imgRes) // Bundle에 데이터 담기
        fragment.arguments = bundle

        fragmentList.add(fragment)
        notifyItemInserted(fragmentList.size - 1)
    }
}
