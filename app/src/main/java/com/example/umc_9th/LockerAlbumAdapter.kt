package com.example.umc_9th

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import umc.study.umc_9th.R
import umc.study.umc_9th.databinding.ItemLockeralbumBinding

class LockerAlbumAdapter(private var albumList : MutableList<LockerAlbumData>, private val onVisitClicked: (LockerSongData) -> Unit) :
    RecyclerView.Adapter<LockerAlbumAdapter.LockerViewHolder>() {

    inner class LockerViewHolder(val binding : ItemLockeralbumBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.Q)
        fun bind(album: LockerAlbumData, position: Int) {
            binding.albumTitle.text = album.title
            binding.albumSinger.text = album.singer
            binding.albumInfo.text = album.info
            binding.albumImg.setImageResource(album.img)
            binding.LsetBtn.setOnClickListener {
                removeItem(position)
            }
            binding.LstartBtn.setOnClickListener {
                if (binding.LstartBtn.tag == R.drawable.btn_miniplay_mvplay) {
                    binding.LstartBtn.setImageResource(R.drawable.btn_miniplay_mvpause)
                    binding.LstartBtn.tag = R.drawable.btn_miniplay_mvpause
                } else {
                    binding.LstartBtn.setImageResource(R.drawable.btn_miniplay_mvplay)
                    binding.LstartBtn.tag = R.drawable.btn_miniplay_mvplay
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LockerAlbumAdapter.LockerViewHolder {
        val binding = ItemLockeralbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LockerViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onBindViewHolder(holder: LockerViewHolder, position: Int) {
        val album = albumList[position]
        holder.bind(album, position)
    }

    override fun getItemCount(): Int {
        return albumList.size
    }
    fun removeItem(position: Int) {
        albumList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, albumList.size)
    }

}