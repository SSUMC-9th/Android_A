package com.example.umc_9th

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import umc.study.umc_8th.databinding.ItemAlbumBinding
import umc.study.umc_8th.databinding.ItemSavedSongBinding

class SavedSongRVAdapter(private val savedSongList: ArrayList<SavedSong>) : RecyclerView.Adapter<SavedSongRVAdapter.ViewHolder>() {
    interface MyItemClickListener{
        fun onItemClick(savedSong: SavedSong)
        fun onRemoveAlbum(position: Int)
    }
    private lateinit var mItemClickListener: MyItemClickListener
    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }

    fun addItem(savedSong: SavedSong){
        savedSongList.add(savedSong) // ?
        notifyItemInserted(savedSongList.size - 1)
    }

    fun removeItem(position: Int){
        savedSongList.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemSavedSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(savedSongList[position])
        holder.itemView.setOnClickListener {
            mItemClickListener.onItemClick(savedSongList[position])
        }

        holder.binding.btnMore.setOnClickListener {
            val pos = holder.bindingAdapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                savedSongList
                notifyItemRemoved(pos)
            }
        }
    }

    override fun getItemCount(): Int = savedSongList.size

    inner class ViewHolder(val binding: ItemSavedSongBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(savedSong: SavedSong){
            binding.itemCoverTitleTv.text = savedSong.title
            binding.itemCoverSingerTv.text = savedSong.singer
            binding.itemLockerCoverImgIv.setImageResource(savedSong.coverImg!!)

            binding.btnMore.setOnClickListener {
                mItemClickListener.onRemoveAlbum(bindingAdapterPosition)
            }
        }
    }
}