package com.example.umc_9th

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.umc_9th.data.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import umc.study.umc_8th.R
import umc.study.umc_8th.databinding.FragmentSavedSongBinding

class SavedSongFragment : Fragment() {
    lateinit var binding: FragmentSavedSongBinding
    private lateinit var songDB: AppDatabase

    private var likedSongs = ArrayList<Song>()
    private lateinit var likedSongAdapter: SavedSongRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedSongBinding.inflate(inflater, container, false)
        songDB = AppDatabase.getInstance(requireContext())

        setupRecyclerView()

        return binding.root
    }
//
//        savedSongDatas.apply {
//            add(SavedSong("LILAC", "아이유 (IU)", R.drawable.img_album_exp2))
//            add(SavedSong("Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp))
//            add(SavedSong("Next Level", "aespa", R.drawable.img_album_exp3))
//            add(SavedSong("Drama", "aespa", R.drawable.img_album_drama))
//            add(SavedSong("Boy with Luv", "방탄소년단 (BTS)", R.drawable.img_album_exp4))
//            add(SavedSong("SUPERNOVA", "aespa", R.drawable.img_album_supernova))
//        }
//
//        val savedSongRVAdapter = SavedSongRVAdapter(savedSongDatas)
//        binding.lockerSavedSongsRv.adapter = savedSongRVAdapter
//        binding.lockerSavedSongsRv.layoutManager =
//            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//
//        return binding.root

    override fun onResume() {
        super.onResume()
        loadLikedSongs()
    }

    private fun setupRecyclerView() {
        likedSongAdapter = SavedSongRVAdapter(likedSongs)
        binding.lockerSavedSongsRv.adapter = likedSongAdapter
        binding.lockerSavedSongsRv.layoutManager = LinearLayoutManager(context)
    }

    private fun loadLikedSongs() {
        lifecycleScope.launch(Dispatchers.IO) { // DB 작업은 IO 스레드
            val songsFromDB = songDB.songDao().getLikedSongs()

            withContext(Dispatchers.Main) { // UI 업데이트는 Main 스레드!
                likedSongs.clear()
                likedSongs.addAll(songsFromDB)
                likedSongAdapter.notifyDataSetChanged()
            }
        }
    }
}
