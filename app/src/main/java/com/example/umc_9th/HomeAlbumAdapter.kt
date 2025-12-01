package com.example.umc_9th

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.umc_9th.data.Album
import com.example.umc_9th.data.Song
import umc.study.umc_8th.databinding.ItemAlbumBinding

class HomeAlbumAdapter(
    private val albumList: List<Album>,
    private val onPlayClick: (Song) -> Unit
) : RecyclerView.Adapter<HomeAlbumAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemAlbumBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(album: Album) {
            binding.itemAlbumTitleTv.text = album.title
            binding.itemAlbumSingerTv.text = album.singer
            binding.itemAlbumPlayImgIv.setOnClickListener {
                // 첫 번째 곡 정보를 전달
                if (album.songs.isNotEmpty()) {
                    onPlayClick(album.songs[0])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(albumList[position])
    }

    override fun getItemCount(): Int = albumList.size
}
