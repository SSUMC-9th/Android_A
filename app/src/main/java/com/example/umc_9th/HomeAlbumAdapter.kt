package com.example.umc_9th

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.umc_9th.data.Album
import com.example.umc_9th.data.Song
import umc.study.umc_8th.R
import umc.study.umc_8th.databinding.ItemAlbumBinding

class HomeAlbumAdapter(
    private val albumList: List<Album>,
    private val onPlayClick: (Song) -> Unit
) : RecyclerView.Adapter<HomeAlbumAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemAlbumBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(album: Album) {
            binding.itemAlbumTitleTv.text = album.title
            binding.itemAlbumSingerTv.text = album.singer

            // 앨범 커버 이미지 설정
            album.coverImg?.let {
                binding.itemAlbumCoverImgIv.setImageResource(it)
            }

            binding.itemAlbumPlayImgIv.setOnClickListener {
                // Album 정보를 기반으로 Song 객체 생성
                val song = Song(
                    id = 0,
                    title = album.title,
                    singer = album.singer,
                    second = 0,
                    playTime = 215, // 기본 재생 시간
                    isPlaying = true,
                    music = R.raw.song_sample, // 기본 음악 파일
                    coverImg = album.coverImg,
                    isLike = album.isLike,
                    albumIdx = album.id
                )
                onPlayClick(song)
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