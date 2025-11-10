package com.example.umc_9th

import android.app.Activity.RESULT_OK
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.SeekBar
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import umc.study.umc_8th.R
import umc.study.umc_8th.databinding.ActivityMainBinding

class ExploreFragment : Fragment(R.layout.fragment_explore)
class SearchFragment : Fragment(R.layout.fragment_search)
class LibraryFragment : Fragment(R.layout.fragment_locker)

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var musicService: MusicService? = null
    private var isBound = false
    private var updateJob: Job? = null
    private lateinit var miniSeekBar: SeekBar

    val songActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val title = result.data?.getStringExtra("title")
            val artist = result.data?.getStringExtra("artist")
            val albumResId = result.data?.getIntExtra("albumResId", R.drawable.btn_textbox_close)

            binding.miniPlayerTitle.text = title
            binding.miniPlayerArtist.text = artist
            albumResId?.let {
                binding.miniPlayerAlbum.setImageResource(it)
                binding.miniPlayerAlbum.tag = it
            }
            // 서비스 상태로 한 번 더 동기화
            if (isBound) refreshFromService()
        }
    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MusicBinder
            musicService = binder.getService()
            isBound = true

            val (title, artist, albumResId) = musicService!!.getCurrentSongInfo()
            title?.let { binding.miniPlayerTitle.text = it }
            artist?.let { binding.miniPlayerArtist.text = it }
            albumResId?.let { binding.miniPlayerAlbum.setImageResource(it) }

            // 재생 상태/위치 즉시 반영 (일시정지 포함)
            val playback = musicService!!.getCurrentPlaybackStatus()
            miniSeekBar.max = playback["duration"] as Int
            miniSeekBar.progress = playback["position"] as Int
            val playing = playback["isPlaying"] as Boolean
            if (playing) {
                binding.miniPlayerPlayButton.setImageResource(R.drawable.nugu_btn_pause_32)
            } else {
                binding.miniPlayerPlayButton.setImageResource(R.drawable.btn_miniplayer_play)
            }

            initMiniSeekBar()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        miniSeekBar = findViewById(R.id.mini_seekbar)
        val serviceIntent = Intent(this, MusicService::class.java)
        ContextCompat.startForegroundService(this, serviceIntent)
        bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE)

        miniSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) musicService?.seekTo(progress)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, HomeFragment())
                .commit()
        }

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            val selectedFragment: Fragment = when (item.itemId) {
                R.id.bottom_homeButton -> HomeFragment()
                R.id.bottom_lookButton -> ExploreFragment()
                R.id.bottom_searchButton -> SearchFragment()
                R.id.bottom_lockerButton -> LockerFragment()
                else -> HomeFragment()
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.container, selectedFragment)
                .commit()

            true
        }

        binding.miniPlayerPlayButton.setOnClickListener {
            if (musicService == null) return@setOnClickListener
            if (musicService!!.isPlaying()) {
                musicService!!.pause()
                binding.miniPlayerPlayButton.setImageResource(R.drawable.btn_miniplayer_play)
            } else {
                musicService!!.play()
                binding.miniPlayerPlayButton.setImageResource(R.drawable.nugu_btn_pause_32)
            }
        }

        binding.miniPlayerNextButton.setOnClickListener {
            musicService?.playNext()
        }

        binding.miniPlayerPreviousButton.setOnClickListener {
            musicService?.playPrev()
        }

        binding.gotoSong.setOnClickListener {
            val title = binding.miniPlayerTitle.text.toString()
            val artist = binding.miniPlayerArtist.text.toString()
            val albumResId = binding.miniPlayerAlbum.tag as? Int

            val intent = Intent(this, SongActivity::class.java).apply {
                putExtra("title", title)
                putExtra("artist", artist)
                putExtra("albumResId", albumResId)
            }
            songActivityLauncher.launch(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        if (isBound) refreshFromService()
    }

    fun updateMiniPlayer(title: String, artist: String, albumResId: Int) {
        binding.miniPlayerTitle.text = title
        binding.miniPlayerArtist.text = artist
        binding.miniPlayerAlbum.setImageResource(albumResId)
        binding.miniPlayerAlbum.tag = albumResId
    }

    private fun initMiniSeekBar() {
        miniSeekBar.max = musicService?.getDuration() ?: 0
        updateMiniSeekBar()
    }

    private fun updateMiniSeekBar() {
        updateJob?.cancel()
        updateJob = lifecycleScope.launch(Dispatchers.Main) {
            while (isBound) {
                // ★ 재생 중 조건 제거: 일시정지여도 현재 위치 유지 표시
                val pos = musicService?.getCurrentPosition() ?: 0
                miniSeekBar.progress = pos
                delay(500)
            }
        }
    }

    private fun refreshFromService() {
        val info = musicService?.getCurrentSongInfo() ?: return
        val play = musicService?.getCurrentPlaybackStatus() ?: return

        info.first?.let { binding.miniPlayerTitle.text = it }
        info.second?.let { binding.miniPlayerArtist.text = it }
        info.third?.let { binding.miniPlayerAlbum.setImageResource(it) }

        miniSeekBar.max = play["duration"] as Int
        miniSeekBar.progress = play["position"] as Int

        val playing = play["isPlaying"] as Boolean
        if (playing) {
            binding.miniPlayerPlayButton.setImageResource(R.drawable.nugu_btn_pause_32)
        } else {
            binding.miniPlayerPlayButton.setImageResource(R.drawable.btn_miniplayer_play)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isBound) {
            unbindService(connection)
            isBound = false
        }
        updateJob?.cancel()
    }
}
