package com.example.umc_9th

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import umc.study.umc_9th.databinding.FragmentAlbumBinding
import umc.study.umc_9th.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {

    private lateinit var binding : FragmentDetailBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        var title : String = FragmentAlbumBinding.inflate(inflater, container, false).albumTitle.text.toString()
        var artist: String = FragmentAlbumBinding.inflate(inflater, container, false).albumSinger.text.toString()
        binding.detailText.text = "이 앨범의 이름은 $title 입니다.\n이 앨범의 작곡가는 $artist 입니다."
        if(artist == "IU") {
            binding.detailText.text = "이 앨범의 이름은 $title 입니다.\n이 앨범의 작곡가는 $artist 입니다.\n\n대동제 아이유 초청 기원 1234일차"
        }
        return binding.root
    }

}