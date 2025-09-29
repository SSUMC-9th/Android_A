package com.example.umc_9th

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class AlbumFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val tv = TextView(requireContext())
        tv.text = "저장앨범 화면"
        tv.gravity = Gravity.CENTER
        return tv
    }
}
