package com.example.umc_9th

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.umc_9th.data.firebase.FirebaseManager
import umc.study.umc_8th.R

class SavedAlbumFragment : Fragment() {

    private lateinit var firebaseManager: FirebaseManager
    private lateinit var adapter: SavedAlbumAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_saved_album, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseManager = FirebaseManager.getInstance()
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView_saved_albums)

        adapter = SavedAlbumAdapter(mutableListOf()) { albumId ->
            // 삭제
            firebaseManager.removeLikedAlbum(albumId,
                onSuccess = { loadAlbums() },
                onFailure = {}
            )
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        loadAlbums()
    }

    override fun onResume() {
        super.onResume()
        loadAlbums()
    }

    private fun loadAlbums() {
        firebaseManager.getLikedAlbums(
            onSuccess = { albums ->
                activity?.runOnUiThread {
                    val savedAlbums = albums.map {
                        SavedAlbum(it.id, it.albumResId, it.title, it.artist)
                    }.toMutableList()
                    adapter.updateAlbums(savedAlbums)
                }
            },
            onFailure = {}
        )
    }
}