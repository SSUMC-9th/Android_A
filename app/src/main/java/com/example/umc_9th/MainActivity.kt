package com.example.umc_9th

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.umc_9th.data.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import umc.study.umc_8th.R
import umc.study.umc_8th.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var songDB: AppDatabase

    // SharedPreferences의 이름과 키를 상수로 정의
    companion object{
        private const val PREF_NAME = "music_pref"
        private const val KEY_LAST_SONG_ID = "last_songId"
    }

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

        songDB = AppDatabase.getInstance(this)

        initNavigation()
        initMIniplayerClickListener()

        loadLastPlayedSong()
        observePlayerState()


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

    private fun initNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_frm) as NavHostFragment
        val navController = navHostFragment.navController
        binding.mainBnv.setupWithNavController(navController)
    }

    private fun initMIniplayerClickListener() {
        binding.mainPlayerCl.setOnClickListener {
            startActivity(Intent(this, SongActivity::class.java))
        }

        binding.mainMiniplayerNextBtn.setOnClickListener {
            MusicPlayerManager.playNext()
        }
        binding.mainMiniplayerPreviousBtn.setOnClickListener {
            MusicPlayerManager.playPrevious()
        }
    }

    fun updateMiniPlayer(song: Song) {
        // 1. MusicPlayerManager로 노래 재생
        MusicPlayerManager.loadSong(this, song)
        MusicPlayerManager.play()

        // 2. SharedPreferences에 현재 곡의 ID 저장
        val sharedPref = getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        sharedPref.edit().apply(){
            putInt(KEY_LAST_SONG_ID, song.id)
            apply()
        }

        Log.d("MainActivity", "Saved songId: ${song.id}")
    }

    private fun loadLastPlayedSong(){
        // 이미 음악이 재생 중이면 이 로직을 실행할 필요 없음
        if (MusicPlayerManager.currentSong == null) {
            return
        }

        val sharedPref = getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        val lastSongId = sharedPref.getInt(KEY_LAST_SONG_ID, -1) // 저장된 ID (없으면 -1)

        if (lastSongId== -1){
            Log.d("MainActivity", "No last songId found")
            return
        }

        // DB 작업은 반드시 백그라운드 스레드(IO)에서 실행
        lifecycleScope.launch(Dispatchers.IO) {
            val lastSong = songDB.songDao().getSong(lastSongId)

            if (lastSong != null) {
                // UI(Manager) 업데이트는 메인 스레드에서
                withContext(Dispatchers.Main) {
                    MusicPlayerManager.loadSong(applicationContext, lastSong)
                    MusicPlayerManager.pause()
                }
            }
        }
    }

    private fun observePlayerState() {
        lifecycleScope.launch {
            // 1. 재생 중인 곡이 바뀌면 UI 업데이트
            MusicPlayerManager.currentSongFlow.collect { song ->
                if (song != null) {
                    binding.mainPlayerCl.visibility = View.VISIBLE
                    binding.mainMiniplayerTitleTv.text = song.title
                    binding.mainMiniplayerSingerTv.text = song.singer
                    binding.mainMiniplayerProgressSb.max = song.playTime * 1000
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