package com.example.umc_9th

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.gson.Gson
import umc.study.umc_8th.R
import umc.study.umc_8th.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    lateinit var binding : FragmentHomeBinding
    private lateinit var albumRVAdapter: AlbumRVAdapter
    private var albumDatas = ArrayList<Album>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // 데이터 리스트 생성 더미 데이터
        albumDatas.apply {
            add(Album("LILAC", "아이유 (IU)", R.drawable.img_album_exp2))
            add(Album("Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp))
            add(Album("Next Level", "aespa", R.drawable.img_album_exp3))
            add(Album("Drama", "aespa", R.drawable.img_album_drama))
            add(Album("Boy with Luv", "방탄소년단 (BTS)", R.drawable.img_album_exp4))
            add(Album("SUPERNOVA", "aespa", R.drawable.img_album_supernova))
        }

        // Adapter 초기화 (플레이 버튼 클릭 시 MainActivity의 미니플레이어 업데이트)
        albumRVAdapter = AlbumRVAdapter(albumDatas) { album ->
            (activity as? MainActivity)?.updateMiniPlayer(album)
        }

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

        return binding.root
    }

    // 앨범 클릭 시 상세 페이지로 이동
    private fun changeAlbumFragment(album: Album) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, AlbumFragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val albumJson = gson.toJson(album)
                    putString("album", albumJson)
                }
            })
            .commitAllowingStateLoss()
    }
}