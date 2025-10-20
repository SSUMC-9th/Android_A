package com.example.umc_9th

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import umc.study.umc_8th.R

data class SavedSong(
    val album: Int,
    val title: String,
    val artist: String
)

class SavedSongAdapter(
    private val items: MutableList<SavedSong>,
) : RecyclerView.Adapter<SavedSongAdapter.SongViewHolder>() {

    inner class SongViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val album: ImageView = view.findViewById(R.id.savedSong_album)
        val title: TextView = view.findViewById(R.id.savedSong_title)
        val artist: TextView = view.findViewById(R.id.savedSong_artist)
        val playButton: ImageButton = view.findViewById(R.id.savedSong_btn_play)
        val moreButton: ImageButton = view.findViewById(R.id.savedSong_btn_more)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_saved_song, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = items[position]
        holder.album.setImageResource(song.album)
        holder.title.text = song.title
        holder.artist.text = song.artist

        var isPlaying = false
        holder.playButton.setOnClickListener {
            isPlaying = !isPlaying
            if (isPlaying) {
                holder.playButton.setImageResource(R.drawable.nugu_btn_pause_32)
            } else {
                holder.playButton.setImageResource(R.drawable.nugu_btn_play_32)
            }
        }

        holder.moreButton.setOnClickListener {
            val pos = holder.bindingAdapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                items.removeAt(pos)
                notifyItemRemoved(pos)
            }
        }
    }

    override fun getItemCount(): Int = items.size
}