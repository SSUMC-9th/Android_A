package com.example.umc_9th

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import umc.study.umc_8th.R

class TrackListFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_track_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView_tracks)

        val tracks = listOf(
            Track(1, "Lady", "Kenshi Yonezu"),
            Track(2, "Spinning Globe", "Kenshi Yonezu"),
            Track(3, "Pop Song", "Kenshi Yonezu")
        )

        val adapter = TrackAdapter(tracks,
            onPlayClick = { track ->
                // TODO: 재생 버튼 클릭 로직
            },
            onMoreClick = { track ->
                // TODO: ... 버튼 클릭 로직
            })

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }
}
