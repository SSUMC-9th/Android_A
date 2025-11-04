package com.example.umc_9th

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import umc.study.umc_8th.databinding.ItemSongsBinding

class SongsRVAdapter(private val songsList: ArrayList<Songs>) : RecyclerView.Adapter<SongsRVAdapter.ViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemSongsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SongsRVAdapter.ViewHolder, position: Int) {
        holder.bind(songsList[position])
    }

    override fun getItemCount(): Int = songsList.size

    inner class ViewHolder(val binding: ItemSongsBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(songs: Songs) {
            binding.itemSongsNumberTv.text = songs.number.toString()
            binding.itemSongsTitleTv.text = songs.title
            binding.itemSongsSingerTv.text = songs.singer
        }
    }
}