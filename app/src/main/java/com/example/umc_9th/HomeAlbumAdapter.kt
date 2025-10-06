package com.example.umc_9th

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import umc.study.umc_9th.R
import umc.study.umc_9th.databinding.ItemTodaymusicBinding

class HomeAlbumAdapter(private var albumList : MutableList<AlbumData>,
                       private val onVisitClicked: (album:AlbumData, button:ButtonType, title:String, singer:String) -> Unit) :
    RecyclerView.Adapter<HomeAlbumAdapter.AlbumViewHolder>() {
    enum class ButtonType {Album, Play}
    inner class AlbumViewHolder(val binding : ItemTodaymusicBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(album: AlbumData) {

            binding.albumTitle.text = album.title
            binding.albumSinger.text = album.singer
            binding.MusicImage.setImageResource(album.image)
            binding.MusicImage.setOnClickListener {
                onVisitClicked(album, ButtonType.Album, binding.albumTitle.text.toString(), binding.albumSinger.text.toString())
            }
            binding.playBtn.setOnClickListener {
                onVisitClicked(album, ButtonType.Play, binding.albumTitle.text.toString(), binding.albumSinger.text.toString())
            }
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AlbumViewHolder {
        val binding = ItemTodaymusicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlbumViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: AlbumViewHolder,
        position: Int
    ) {
        val album = albumList[position]
        holder.bind(album)
    }

    override fun getItemCount(): Int {
        return albumList.size
    }

}