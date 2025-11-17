package com.example.umc_9th

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.umc_9th.data.firebase.FirebaseManager
import umc.study.umc_8th.R

class SavedSongFragment : Fragment() {

    private lateinit var firebaseManager: FirebaseManager
    private lateinit var adapter: SavedSongAdapter
    private lateinit var recyclerView: RecyclerView
    //private lateinit var emptyView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_saved_song, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseManager = FirebaseManager.getInstance()

        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView_saved_songs)

        val savedSongs = mutableListOf<SavedSong>()

        adapter = SavedSongAdapter(mutableListOf())
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        loadLikedSongs()

    }

    override fun onResume() {
        super.onResume()
        loadLikedSongs()
    }

    private fun loadLikedSongs() {
        firebaseManager.getLikedSongs(
            onSuccess = { likedSongs ->
                activity?.runOnUiThread {
                    if (likedSongs.isEmpty()) {
                        recyclerView.visibility = View.GONE
                        // emptyView?.visibility = View.VISIBLE
                    } else {
                        recyclerView.visibility = View.VISIBLE
                        // emptyView?.visibility = View.GONE

                        val savedSongs = likedSongs.map { song ->
                            SavedSong(
                                album = song.coverImg ?: R.drawable.img_album_exp,
                                title = song.title,
                                artist = song.singer
                            )
                        }.toMutableList()

                        adapter.updateSongs(savedSongs)
                    }
                }
            },
            onFailure = { error ->
                activity?.runOnUiThread {
                    android.util.Log.e("SavedSongFragment", "Firebase 오류: $error")
                    android.widget.Toast.makeText(
                        context,
                        "데이터 로드 실패: $error",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
    }
}
