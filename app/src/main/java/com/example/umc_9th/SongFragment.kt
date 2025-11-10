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
import umc.study.umc_8th.databinding.FragmentSongBinding

class SongFragment : Fragment() {
    lateinit var binding: FragmentSongBinding
    private lateinit var songDB: AppDatabase

    private var songList = ArrayList<Song>()
    private lateinit var songsRVAdapter: SongsRVAdapter

    private var albumId: Int = 0 // 부모(AlbumFragment)로부터 받을 앨범 ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ 4. arguments에서 앨범 ID를 꺼냄
        arguments?.let {
            albumId = it.getInt("albumIdKey") // (AlbumFragment에서 넘겨줄 키 이름)
        }

        songDB = AppDatabase.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSongBinding.inflate(inflater, container, false)

        setupRecyclerView()

        return binding.root

//        songsDatas.apply {
//            add(Songs(1, "LILAC", "아이유 (IU)"))
//            add(Songs(2, "flu", "아이유 (IU)"))
//            add(Songs(3, "Coin", "아이유 (IU)"))
//            add(Songs(4, "봄 안녕 봄", "아이유 (IU)"))
//            add(Songs(5, "Celebrity", "아이유 (IU)"))
//            add(Songs(6, "돌림노래", "아이유 (IU)"))
//        }
//
//        val songsRVAdapter = SongsRVAdapter(songsDatas)
//        binding.songsRv.adapter = songsRVAdapter
//        binding.songsRv.layoutManager =
//            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//
    }

    override fun onResume() {
        super.onResume()
        loadAlbumSongs(albumId)
    }
    private fun setupRecyclerView() {
        // 어댑터를 '빈 리스트'로 먼저 초기화
        songsRVAdapter = SongsRVAdapter(songList)
        binding.songsRv.adapter = songsRVAdapter
        binding.songsRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    // DB에서 '좋아요' 누른 곡만 불러오는 함수
    private fun loadAlbumSongs(albumId: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            // 8. DB에 "이 앨범 ID에 해당하는 곡만 줘"라고 요청 (백그라운드)
            val songsFromDB = songDB.songDao().getSongsInAlbum(albumId)

            // 9. 메인 스레드에서 UI 갱신
            withContext(Dispatchers.Main) {
                songList.clear()
                songList.addAll(songsFromDB)
                songsRVAdapter.notifyDataSetChanged()
            }
        }
    }
}
