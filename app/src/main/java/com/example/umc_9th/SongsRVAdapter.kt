package com.example.umc_9th

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.umc_9th.data.Song
import umc.study.umc_8th.databinding.ItemSongsBinding

class SongsRVAdapter(private val songList: ArrayList<Song>) :
    RecyclerView.Adapter<SongsRVAdapter.ViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemSongsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SongsRVAdapter.ViewHolder, position: Int) {
        holder.bind(songList[position], position)
    }

    override fun getItemCount(): Int = songList.size

    inner class ViewHolder(val binding: ItemSongsBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(song: Song, position: Int) {
            binding.itemSongsNumberTv.text = (position + 1).toString()
            binding.itemSongsTitleTv.text = song.title
            binding.itemSongsSingerTv.text = song.singer
        }
    }
}