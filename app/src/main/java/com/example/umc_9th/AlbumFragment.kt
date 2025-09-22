package com.redcaramel.umc_misson_2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.redcaramel.umc_misson_2.databinding.FragmentAlbumBinding

class AlbumFragment : Fragment() {
    private lateinit var binding : FragmentAlbumBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            val title = it.getString("title")
            val singer = it.getString("singer")
            binding.albumTitle.text = title
            binding.albumSinger.text = singer
        }
        binding = FragmentAlbumBinding.inflate(layoutInflater)
        binding.albumBackButton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, HomeFragment())
                .commit()
        }
        return binding.root
    }

}