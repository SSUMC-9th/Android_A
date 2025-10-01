package com.example.umc_9th

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import umc.study.umc_8th.R

data class Album(
    val title: String,
    val artist: String,
    val albumResId: Int
)

class HomeAlbumAdapter(
    private val items: List<Album>,
    private val onAlbumClick: (Album) -> Unit
) : RecyclerView.Adapter<HomeAlbumAdapter.HomeAlbumViewHolder>() {

    inner class HomeAlbumViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.recyclerView_album)
        val title: TextView = view.findViewById(R.id.recyclerView_album_title)
        val artist: TextView = view.findViewById(R.id.recyclerView_album_artist)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAlbumViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_album, parent, false)
        return HomeAlbumViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeAlbumViewHolder, position: Int) {
        val album = items[position]
        holder.image.setImageResource(album.albumResId)
        holder.title.text = album.title
        holder.artist.text = album.artist

        holder.itemView.setOnClickListener {
            onAlbumClick(album)
        }
    }

    override fun getItemCount(): Int = items.size
}