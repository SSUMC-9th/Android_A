package com.example.umc_9th

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import umc.study.umc_8th.R
import umc.study.umc_8th.databinding.FragmentLockerBinding

class LockerFragment : Fragment() {

    private val information = arrayListOf("저장한 곡", "음악파일", "저장앨범")

    private var savedSongData = ArrayList<SavedSong>()

    lateinit var binding: FragmentLockerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLockerBinding.inflate(inflater, container, false)

        val lockerAdapter = LockerVPAdapter(this)
        binding.lockerContentVp.adapter = lockerAdapter

        TabLayoutMediator(binding.lockerContentTb, binding.lockerContentVp) { tab, position ->
            tab.text = information[position]
        }.attach()

        return binding.root
    }

}

