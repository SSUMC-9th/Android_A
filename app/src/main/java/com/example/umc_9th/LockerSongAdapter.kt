package com.example.umc_9th

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import umc.study.umc_9th.databinding.ItemLockersongBinding

class LockerSongAdapter(private var songList : MutableList<LockerSongData>,
                       private val onVisitClicked: (LockerSongData) -> Unit) :
    RecyclerView.Adapter<LockerSongAdapter.LockerViewHolder>() {

    inner class LockerViewHolder(val binding : ItemLockersongBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(song: LockerSongData, position: Int) {
            binding.Ltitle.text = song.title
            binding.Lsinger.text = song.singer
            binding.songImg.setImageResource(song.img)
            binding.LsetBtn.setOnClickListener {
                removeItem(position)
            }
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LockerViewHolder {
        val binding = ItemLockersongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LockerViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: LockerViewHolder,
        position: Int
    ) {
        val album = songList[position]
        holder.bind(album,position)
    }

    override fun getItemCount(): Int {
        return songList.size
    }
    fun removeItem(position: Int) {
        songList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, songList.size)
    }
}