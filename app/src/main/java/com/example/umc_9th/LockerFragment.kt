package com.example.umc_9th

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.umc_9th.data.Song
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import umc.study.umc_8th.R
import umc.study.umc_8th.databinding.FragmentLockerBinding

class LockerFragment : Fragment() {

    private val information = arrayListOf("저장한 곡", "음악파일", "저장앨범")

//    private var savedSongData = ArrayList<SavedSong>()

    lateinit var binding: FragmentLockerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLockerBinding.inflate(inflater, container, false)

        setupViewPager()

        binding.tvLockerLogin.setOnClickListener {
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        val token = Prefs.getToken(requireActivity())

        if (token.isNotEmpty()) {
            binding.tvLockerLogin.text = "로그아웃"

            binding.tvLockerLogin.setOnClickListener {
                logout()
            }
        } else {
            binding.tvLockerLogin.text = "로그인"

            binding.tvLockerLogin.setOnClickListener {
                startActivity(Intent(requireActivity(), LoginActivity::class.java))
            }
        }
    }

    private fun logout() {
        Prefs.clearToken(requireActivity())
        binding.tvLockerLogin.text = "로그인"

        binding.tvLockerLogin.setOnClickListener {
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
        }
    }

    private fun setupViewPager() {
        val lockerAdapter = LockerVPAdapter(this)
        binding.lockerContentVp.adapter = lockerAdapter

        TabLayoutMediator(binding.lockerContentTb, binding.lockerContentVp) { tab, position ->
            tab.text = information[position]
        }.attach()
    }
}

