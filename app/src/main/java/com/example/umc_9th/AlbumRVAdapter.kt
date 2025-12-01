package com.example.umc_9th

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.umc_9th.data.Album
import umc.study.umc_8th.databinding.ItemAlbumBinding

class AlbumRVAdapter(
    private val albumList: ArrayList<Album>,
    private val onPlayClick: (Album) -> Unit // ✅ 플레이버튼 콜백 추가
) : RecyclerView.Adapter<AlbumRVAdapter.ViewHolder>(){

    interface MyItemClickListener{
        fun onItemClick(album: Album)
        fun onRemoveAlbum(position: Int)
    }
    private lateinit var mItemClickListener: MyItemClickListener
    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }

    fun addItem(album: Album){
        albumList.add(album)
        notifyDataSetChanged()
    }
//
    fun removeItem(position: Int){
        albumList.removeAt(position)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AlbumRVAdapter.ViewHolder {
        val binding: ItemAlbumBinding = ItemAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    // onBindViewHolder가 포지션 값 갖고 있기에 보통 클릭이벤트 여기서 작성
    override fun onBindViewHolder(holder: AlbumRVAdapter.ViewHolder, position: Int) {
        holder.bind(albumList[position])
//        holder.itemView.setOnClickListener {
//            mItemClickListener.onItemClick(albumList[position])
//        }
    }

    override fun getItemCount(): Int = albumList.size

    inner class ViewHolder(val binding: ItemAlbumBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(album: Album) {
            binding.itemAlbumTitleTv.text = album.title
            binding.itemAlbumSingerTv.text = album.singer
            binding.itemAlbumCoverImgIv.setImageResource(album.coverImg!!)

            // 앨범 클릭 시 상세 페이지로 이동
            binding.root.setOnClickListener {
                mItemClickListener.onItemClick(album)
            }
            // 플레이 버튼 클릭 시 콜백 호출
            binding.itemAlbumPlayImgIv.setOnClickListener {
                onPlayClick(album)
            }
        }
    }
}