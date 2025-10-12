package com.example.umc_9th

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import umc.study.umc_8th.R
import umc.study.umc_8th.databinding.ItemSavedAlbumBinding
import umc.study.umc_8th.databinding.ItemSavedSongBinding

class SavedAlbumRVAdapter(private val savedAlbumList: ArrayList<SavedAlbum>) : RecyclerView.Adapter<SavedAlbumRVAdapter.ViewHolder>() {
    interface MyItemClickListener{
        fun onItemClick(savedSong: SavedSong)
        fun onRemoveAlbum(position: Int)
    }
    private lateinit var mItemClickListener: SavedSongRVAdapter.MyItemClickListener
    fun setMyItemClickListener(itemClickListener: SavedSongRVAdapter.MyItemClickListener){
        mItemClickListener = itemClickListener
    }

    fun addItem(savedAlbum: SavedAlbum){
        savedAlbumList.add(savedAlbum)
        notifyItemInserted(savedAlbumList.size - 1)
    }

    fun removeItem(position: Int){
        savedAlbumList.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemSavedAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SavedAlbumRVAdapter.ViewHolder, position: Int) {
        holder.bind(savedAlbumList[position])
    }

    override fun getItemCount(): Int = savedAlbumList.size
    inner class ViewHolder(val binding: ItemSavedAlbumBinding):
        RecyclerView.ViewHolder(binding.root) {

        private var isPlaying = false // ✅ 클릭 상태 저장용 변수

        fun bind(savedAlbum: SavedAlbum) {
            binding.itemSavedAlbumTitleTv.text = savedAlbum.albumName
            binding.itemSavedAlbumSingerTv.text = savedAlbum.singer
            binding.itemSavedAlbumInfoTv.text = savedAlbum.info
            binding.itemSavedAlbumImgIv.setImageResource(savedAlbum.coverImg!!)

            binding.itemSavedAlbumPlayIv.setOnClickListener {
                if (isPlaying) {
                    binding.itemSavedAlbumPlayIv.setImageResource(R.drawable.btn_miniplayer_play)
                } else {
                    binding.itemSavedAlbumPlayIv.setImageResource(R.drawable.btn_miniplay_pause)
                }
                isPlaying = !isPlaying // 상태 반전
            }

            binding.btnMore.setOnClickListener {
                mItemClickListener.onRemoveAlbum(bindingAdapterPosition)
            }
        }
    }
}