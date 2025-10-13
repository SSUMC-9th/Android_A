package com.example.umc_9th

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import umc.study.umc_9th.databinding.ItemAlbumsongBinding

class AlbumSongAdapter(private var songList : MutableList<AlbumSongData>,
                       private val onVisitClicked: (AlbumSongData) -> Unit) :
    RecyclerView.Adapter<AlbumSongAdapter.AlbumViewHolder>() {

    inner class AlbumViewHolder(val binding : ItemAlbumsongBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(song: AlbumSongData) {
            if(song.num>=10) binding.musicNum.text = song.num.toString()
            else binding.musicNum.text = "0${song.num}"
            binding.title.text = song.title
            binding.singer.text = song.singer
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AlbumViewHolder {
        val binding = ItemAlbumsongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlbumViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: AlbumViewHolder,
        position: Int
    ) {
        val album = songList[position]
        holder.bind(album)
    }

    override fun getItemCount(): Int {
        return songList.size
    }

}