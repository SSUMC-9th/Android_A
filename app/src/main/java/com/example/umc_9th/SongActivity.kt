package com.example.umc_9th

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import umc.study.umc_8th.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {
    lateinit var binding: ActivitySongBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra("title") && intent.hasExtra("singer")) {
            binding.songMusicTitleTv.text = intent.getStringExtra("title")
            binding.songSingerNameTv.text = intent.getStringExtra("singer")
        }

        // 닫기 버튼 → MainActivity로 데이터 전달
        binding.songDownIb.setOnClickListener {
            val returnIntent = Intent().apply {
                putExtra("albumTitle", binding.songMusicTitleTv.text.toString()) // ✅ 앨범 제목 전달
            }
            setResult(RESULT_OK, returnIntent)
            finish()
        }

        binding.songMiniplayerIv.setOnClickListener { setPlayerStatus(false) }
        binding.songPauseIv.setOnClickListener { setPlayerStatus(true) }
    }

    fun setPlayerStatus(isPlaying: Boolean) {
        if (isPlaying) {
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
        } else {
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
        }
    }
}