package com.example.umc_9th

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.umc_9th.data.Song
import umc.study.umc_8th.databinding.ItemSavedSongBinding

class SavedSongRVAdapter(private val songList: ArrayList<Song>) : RecyclerView.Adapter<SavedSongRVAdapter.ViewHolder>() {
    interface MyItemClickListener{
        fun onItemClick(song: Song)
        fun onRemoveAlbum(position: Int)
    }
    private lateinit var mItemClickListener: MyItemClickListener
    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }

    fun updateList(newList: List<Song>){
        songList.clear()
        songList.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemSavedSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(songList[position])
        holder.itemView.setOnClickListener {
            mItemClickListener.onItemClick(songList[position])
        }
    }

    override fun getItemCount(): Int = songList.size

    inner class ViewHolder(val binding: ItemSavedSongBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(song: Song){
            binding.itemCoverTitleTv.text = song.title
            binding.itemCoverSingerTv.text = song.singer
            binding.itemLockerCoverImgIv.setImageResource(song.coverImg!!)

            binding.btnMore.setOnClickListener {
                mItemClickListener.onRemoveAlbum(bindingAdapterPosition)
            }
        }
    }
}