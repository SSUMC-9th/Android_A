package com.example.umc_9th

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.umc_9th.Song
import com.example.umc_9th.SongActivity
import kotlinx.coroutines.launch
import umc.study.umc_8th.R
import umc.study.umc_8th.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    // SongActivity 결과를 받는 콜백 등록
    private val songActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val albumTitle = result.data?.getStringExtra("albumTitle")
                albumTitle?.let {
                    Toast.makeText(this, "받은 앨범 제목: $it", Toast.LENGTH_SHORT).show()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 미니플레이어 클릭 리스너를 추가
        binding.mainPlayerCl.setOnClickListener {
            startActivity(Intent(this, SongActivity::class.java))
        }

        // observePlayerState 함수를 호출
        observePlayerState()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_frm) as NavHostFragment
        val navController = navHostFragment.navController

        binding.mainBnv.setupWithNavController(navController)
    }

    fun updateMiniPlayer(song: Song) {
        MusicPlayerManager.loadSong(this, song)
        MusicPlayerManager.play()
    }

    private fun observePlayerState() {
        lifecycleScope.launch {
            // 1. 재생 중인 곡이 바뀌면 UI 업데이트
            MusicPlayerManager.currentSongFlow.collect { song ->
                if (song != null) {
                    binding.mainPlayerCl.visibility = View.VISIBLE
                    binding.mainMiniplayerTitleTv.text = song.title
                    binding.mainMiniplayerSingerTv.text = song.singer
                    binding.mainMiniplayerProgressSb.max = song.playtime * 1000
                } else {
                    binding.mainPlayerCl.visibility = View.GONE
                }
            }
        }

        lifecycleScope.launch {
            // 2. 재생 상태(재생/멈춤)가 바뀌면 버튼 아이콘 업데이트
            MusicPlayerManager.isPlaying.collect { isPlaying ->
                if (isPlaying) {
                    binding.mainMiniplayerBtn.visibility = View.GONE
                    binding.mainPauseBtn.visibility = View.VISIBLE
                } else {
                    binding.mainMiniplayerBtn.visibility = View.VISIBLE
                    binding.mainPauseBtn.visibility = View.GONE
                }
            }
        }

        lifecycleScope.launch {
            // 3. 재생 시간이 바뀌면 SeekBar 업데이트
            MusicPlayerManager.playbackPosition.collect { position ->
                binding.mainMiniplayerProgressSb.progress = position
            }
        }
    }
}