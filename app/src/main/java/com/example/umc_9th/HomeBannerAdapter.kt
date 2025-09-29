package com.example.umc_9th

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import umc.study.umc_8th.R

class HomeBannerAdapter(
    private val items: List<BannerData>,
    private val onSongClick: (BannerSong) -> Unit
) : RecyclerView.Adapter<HomeBannerAdapter.BannerViewHolder>() {

    inner class BannerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.homeTopText)
        val playButton: ImageView = view.findViewById(R.id.home_panel_play_button)

        // song1
        val song1Layout: LinearLayout = view.findViewById(R.id.song1_container)
        val song1Title: TextView = view.findViewById(R.id.song1_title)
        val song1Artist: TextView = view.findViewById(R.id.song1_artist)

        // song2
        val song2Layout: LinearLayout = view.findViewById(R.id.song2_container)
        val song2Title: TextView = view.findViewById(R.id.song2_title)
        val song2Artist: TextView = view.findViewById(R.id.song2_artist)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_banner, parent, false)
        return BannerViewHolder(view)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        val banner = items[position]

        holder.title.text = banner.title

        // song1 데이터 바인딩
        if (banner.songs.size > 0) {
            val song1 = banner.songs[0]
            holder.song1Title.text = song1.title
            holder.song1Artist.text = song1.artist
            holder.song1Layout.setOnClickListener {
                onSongClick(song1)
            }
        }

        // song2 데이터 바인딩
        if (banner.songs.size > 1) {
            val song2 = banner.songs[1]
            holder.song2Title.text = song2.title
            holder.song2Artist.text = song2.artist
            holder.song2Layout.setOnClickListener {
                onSongClick(song2)
            }
        }
    }

    override fun getItemCount(): Int = items.size
}
