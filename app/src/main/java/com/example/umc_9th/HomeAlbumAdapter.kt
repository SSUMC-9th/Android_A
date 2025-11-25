package com.example.umc_9th

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import umc.study.umc_8th.R

data class Album(
    val id: Int = 0,
    val title: String,
    val artist: String,
    val albumResId: Int,
    var isLike: Boolean = false
)

class HomeAlbumAdapter(
    private val items: List<Album>,
    private val onAlbumClick: (Album) -> Unit,
    private val onPlayClick: (Album) -> Unit
) : RecyclerView.Adapter<HomeAlbumAdapter.AlbumViewHolder>() {

    inner class AlbumViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.recyclerView_album)
        val title: TextView = view.findViewById(R.id.recyclerView_album_title)
        val artist: TextView = view.findViewById(R.id.recyclerView_album_artist)
        val playButton: ImageButton = view.findViewById(R.id.album_play_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_album, parent, false)
        return AlbumViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val album = items[position]
        holder.image.setImageResource(album.albumResId)
        holder.title.text = album.title
        holder.artist.text = album.artist

        holder.itemView.setOnClickListener {
            onAlbumClick(album)
        }
        holder.playButton.setOnClickListener {
            onPlayClick(album)
        }
    }

    override fun getItemCount(): Int = items.size
}