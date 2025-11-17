package com.example.umc_9th

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.umc_9th.data.Album
import com.example.umc_9th.data.Song
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import umc.study.umc_8th.R
import umc.study.umc_8th.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    lateinit var binding : FragmentHomeBinding
    private lateinit var albumRVAdapter: AlbumRVAdapter
    private var albumDatas = ArrayList<Album>()

    private var songList = emptyList<Song>()
    private lateinit var songDB: AppDatabase

    private val autoScrollHandler = Handler(Looper.getMainLooper())
    private val autoScrollRunnable = object : Runnable {
        override fun run() {
            val adapter = binding.homeMainBannerVp.adapter
            if (adapter != null && adapter.itemCount > 0) {
                // 다음 페이지로 이동 (마지막 페이지면 처음으로)
                val nextItem = (binding.homeMainBannerVp.currentItem + 1) % adapter.itemCount
                binding.homeMainBannerVp.setCurrentItem(nextItem, true)
            }
            // 3초 뒤에 다시 이 작업을 실행하도록 예약
            autoScrollHandler.postDelayed(this, 3000)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        songDB = AppDatabase.getInstance(requireContext())

        setupAlbumRV()
        loadSongsFromDB()

        binding.homeTodayMusicAlbumRv.adapter = albumRVAdapter
        binding.homeTodayMusicAlbumRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        // 아이템 클릭 / 삭제 이벤트
        albumRVAdapter.setMyItemClickListener(object: AlbumRVAdapter.MyItemClickListener{
            override fun onItemClick(album: Album) {
                changeAlbumFragment(album)
            }
            override fun onRemoveAlbum(position: Int) {
                albumRVAdapter.removeItem(position)
            }
        })
        val mainBannerAdapter = BannerVPAdapter(this)
        mainBannerAdapter.addFragment(BannerFragment(R.drawable.img_first_album_default))
        mainBannerAdapter.addFragment(BannerFragment(R.drawable.img_album_supernova))
        binding.homeMainBannerVp.adapter = mainBannerAdapter
        binding.homeMainBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        // 배너어댑터
        val bannerAdapter = BannerVPAdapter(this)
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        // CircleIndicator 연결하기
        binding.homeBannerIndicator.setViewPager(binding.homeMainBannerVp)

        return binding.root
    }

    private fun setupAlbumRV(){
    // 데이터 리스트 생성 더미 데이터
        albumDatas.apply {
            add(Album(0,"LILAC", "아이유 (IU)", false, R.drawable.img_album_exp2))
            add(Album(1, "Butter", "방탄소년단 (BTS)", false, R.drawable.img_album_exp))
            add(Album(2, "Next Level", "aespa", false, R.drawable.img_album_exp3))
            add(Album(3, "Drama", "aespa", false,R.drawable.img_album_drama))
            add(Album(4, "Boy with Luv", "방탄소년단 (BTS)", false,R.drawable.img_album_exp4))
            add(Album(5, "SUPERNOVA", "aespa", false,R.drawable.img_album_supernova))
        }

        lifecycleScope.launch(Dispatchers.IO) {
            albumDatas.forEach { album ->
                try {
                    songDB.albumDao().insert(album)
                } catch (e: Exception) {
                    // 이미 존재하면 무시
                }
            }
        }

        albumRVAdapter = AlbumRVAdapter(albumDatas){album ->
            val song = Song(
                0,
                album.title,
                album.singer,
                0,
                215,
                true,
                R.raw.song_sample,
                album.coverImg,
                false,
                0
            )
            (activity as? MainActivity)?.updateMiniPlayer(song)
        }
        binding.homeTodayMusicAlbumRv.adapter = albumRVAdapter
    }

    // DB에서 데이터를 로드하는 함수
    private fun loadSongsFromDB() {
        lifecycleScope.launch(Dispatchers.IO){
            songList = songDB.songDao().getAllSongs()
            withContext(Dispatchers.Main) {
                Log.d("HomeFragment", "loadSongs: $songList")
            }
        }
    }

    // 자동 스크롤 생명주기 관리 (메모리 누수 방지)
    override fun onResume() {
        super.onResume()
        // 화면이 다시 보일 때 3초 뒤 자동 스크롤 시작
        autoScrollHandler.postDelayed(autoScrollRunnable, 3000)
    }

    override fun onPause() {
        super.onPause()
        // 화면이 보이지 않을 때 자동 스크롤 작업 모두 제거
        autoScrollHandler.removeCallbacks(autoScrollRunnable)
    }


    // 앨범 클릭 시 상세 페이지로 이동
    private fun changeAlbumFragment(album: Album) {
        val gson = Gson()
        val albumJson = gson.toJson(album)

        // 1. Bundle을 만듭니다.
        val bundle = Bundle().apply {
            putString("album", albumJson)
        }

        // 2. NavController로 이동합니다. (nav_graph.xml에 정의된 actionId 사용)
        findNavController().navigate(R.id.nav_graph, bundle)
    }
}