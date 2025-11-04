package com.example.umc_9th

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import me.relex.circleindicator.CircleIndicator3
import umc.study.umc_9th.R
import umc.study.umc_9th.databinding.FragmentHomeBinding
import kotlin.concurrent.timer

class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding
    interface OnAlbumButtonClickListener {
        fun onAlbumButtonClicked(title : String, singer : String)
    }
    inner class PagerRunnable:Runnable{
        override fun run() {
            while(true){
                Thread.sleep(3000)
                handler.sendEmptyMessage(0)
            }
        }
    }
    val handler= Handler(Looper.getMainLooper()) {
        setPage()
        true
    }
    private var listener: OnAlbumButtonClickListener? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        listener = context as OnAlbumButtonClickListener?
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)
        var thread = Thread(PagerRunnable())
        thread.start()
        val todayAlbumList = mutableListOf(
            AlbumData("Love wins all", "IU", R.drawable.img_album_lovewinsall),
            AlbumData("Drama", "aespa", R.drawable.img_album_drama),
            AlbumData("Lilac", "IU", R.drawable.img_album_exp2),
            AlbumData("Supernova", "Le Serrapim", R.drawable.img_album_supernova)
        )
        val adapter = HomeAlbumAdapter(todayAlbumList) { album, buttonType, title, singer ->
            when(buttonType) {
                HomeAlbumAdapter.ButtonType.Album -> {
                    val bundle = Bundle().apply {
                        putString("title", album.title)
                        putString("singer", album.singer)
                    }
                    val fragment = AlbumFragment().apply { arguments = bundle }

                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, fragment)
                        .addToBackStack(null)
                        .commit()
                }
                HomeAlbumAdapter.ButtonType.Play -> {
                    listener?.onAlbumButtonClicked(title, singer)
                }
            }

        }
        binding.todayMusicRV.adapter = adapter
        binding.todayMusicRV.layoutManager= LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

        val bannerAdapter = BannerMusicVPAdapter(this)
        bannerAdapter.addFragment(HomeBannerFragment(R.drawable.img_first_album_default))
        bannerAdapter.addFragment(HomeBannerFragment(R.drawable.img_second_album_default))
        binding.homeMusicBanner.adapter = bannerAdapter
        binding.homeMusicBanner.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        binding.bannerIndicator.setViewPager(binding.homeMusicBanner)

        return binding.root

    }
    var bannerNum = 0
    fun setPage() {
        if(bannerNum==2) bannerNum=0
        binding.homeMusicBanner.setCurrentItem(bannerNum,true)
        bannerNum++

    }


}