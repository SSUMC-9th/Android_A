package com.example.umc_9th

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class SavedSongFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val tv = TextView(requireContext())
        tv.text = "저장한 곡 목록"
        tv.gravity = Gravity.CENTER
        return tv
    }
}