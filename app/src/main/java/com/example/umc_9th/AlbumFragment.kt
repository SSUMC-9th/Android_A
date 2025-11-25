package com.example.umc_9th

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.umc_9th.data.Album
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import umc.study.umc_8th.R
import umc.study.umc_8th.databinding.FragmentAlbumBinding

class AlbumFragment : Fragment() {
    private lateinit var binding: FragmentAlbumBinding
    private lateinit var albumDB : AppDatabase

    private var gson: Gson = Gson()

    private val information = arrayListOf("수록곡", "상세정보", "영상")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // binding 먼저 초기화
        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        albumDB = AppDatabase.getInstance(requireContext())

        val albumJson = arguments?.getString("album")
        val album = gson.fromJson(albumJson, Album::class.java)
        setInit(album)

        // 뒤로가기 버튼 클릭 이벤트
        binding.albumBackIv.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, HomeFragment())
                .commit()
        }

        binding.albumLikeIv.setOnClickListener {
            toggleAlbumLike(album)
        }

        val albumAdapter = AlbumVPAdapter(this)
        binding.albumContentVp.adapter = albumAdapter
        TabLayoutMediator(binding.albumContentTb, binding.albumContentVp) { tab, position ->
            tab.text = information[position]
        }.attach()

        return binding.root
    }

    private fun setInit(album: Album) {
        binding.albumAlbumIv.setImageResource(album.coverImg!!)
        binding.albumMusicTitleTv.text = album.title
        binding.albumSingerNameTv.text = album.singer
    }

    private fun toggleAlbumLike(album: Album) {
        album.isLike = !album.isLike

        lifecycleScope.launch(Dispatchers.IO) {
            albumDB.albumDao().updateIsLike(album.id, album.isLike)
        }

        Log.d("IS_LIKE_TEST", "저장 시도: 앨범 ID ${album.id}, 상태: ${album.isLike}")

        if (album.isLike) {
            binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_on)
        } else {
            binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }
    }

}
