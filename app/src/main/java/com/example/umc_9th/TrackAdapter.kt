package com.example.umc_9th

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import umc.study.umc_8th.R

data class Track(
    val number: Int,
    val title: String,
    val artist: String
)

class TrackAdapter(
    private val items: List<Track>,
    private val onPlayClick: (Track) -> Unit,
    private val onMoreClick: (Track) -> Unit
) : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {

    inner class TrackViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val number: TextView = view.findViewById(R.id.track_number)
        val title: TextView = view.findViewById(R.id.track_title)
        val artist: TextView = view.findViewById(R.id.track_artist)
        val playButton: ImageButton = view.findViewById(R.id.track_btn_play)
        val moreButton: ImageButton = view.findViewById(R.id.track_btn_more)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_track, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = items[position]
        holder.number.text = track.number.toString().padStart(2, '0')
        holder.title.text = track.title
        holder.artist.text = track.artist

        holder.playButton.setOnClickListener { onPlayClick(track) }
        holder.moreButton.setOnClickListener { onMoreClick(track) }
    }

    override fun getItemCount(): Int = items.size
}