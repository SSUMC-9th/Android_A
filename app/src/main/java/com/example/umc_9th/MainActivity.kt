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

class SearchFragment : Fragment(R.layout.fragment_search)
class LibraryFragment : Fragment(R.layout.fragment_locker)

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var musicService: MusicService? = null
    private var isBound = false // 서비스 연결 여부
    private var updateJob: Job? = null // seekbar 초당 2번(0.5초마다 업데이트 위한 Job)

    // SongActivity에서 돌아올 때 노래 제목, 가수명, 앨범 이미지 전달하는 건 액티비티 간 데이터 전달하는 로직 대신에
    // Songactivity에서 service 에 넣어서 main에서 받아쓰는 로직으로 수정

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MusicBinder
            musicService = binder.getService()
            isBound = true

            val (title, artist, albumResId) = musicService!!.getCurrentSongInfo()
            title?.let { binding.miniPlayerTitle.text = it }
            artist?.let { binding.miniPlayerArtist.text = it }
            albumResId?.let { binding.miniPlayerAlbum.setImageResource(it) }

            // 서비스 처음 연결된 순간 재생 상태, 위치 반영
            val playback = musicService!!.getCurrentPlaybackStatus()
            binding.miniSeekbar.max = playback["duration"] as Int
            binding.miniSeekbar.progress = playback["position"] as Int
            val playing = playback["isPlaying"] as Boolean
            if (playing) {
                binding.miniPlayerPlayButton.setImageResource(R.drawable.nugu_btn_pause_32)
            } else {
                binding.miniPlayerPlayButton.setImageResource(R.drawable.btn_miniplayer_play)
            }

            updateJob?.cancel()
            updateJob = lifecycleScope.launch(Dispatchers.Main) {
                while (isBound) {
                    binding.miniSeekbar.progress = musicService?.getCurrentPosition() ?: 0
                    refreshFromService()
                    delay(500)
                }
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val serviceIntent = Intent(this, MusicService::class.java)
        ContextCompat.startForegroundService(this, serviceIntent)
        bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE)

        binding.miniSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
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
                R.id.bottom_lookButton -> LookFragment()
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
            musicService?.let { service ->
                val (title, artist, albumResId) = service.getCurrentSongInfo()

                //SongActivity로 넘길 때 연동되도록 제목 가수명 이미지는 intent에 데이터 넣어놓기
                val intent = Intent(this, SongActivity::class.java).apply {
                    putExtra("title", title ?: "Unknown")
                    putExtra("artist", artist ?: "Unknown")
                    putExtra("albumResId", albumResId ?: R.drawable.img_album_exp)
                    // SongId도 필요하면 추가 (좋아요 기능 때문에)
                    // putExtra("songId", currentSongId)
                }
                startActivity(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (isBound) refreshFromService()
    }

    // 0.5초마다 반복: Service 데이터로부터 곡 정보(제목, 가수명, 이미지), seekbar, 재생/일시정지 버튼 업데이트
    private fun refreshFromService() {
        val info = musicService?.getCurrentSongInfo() ?: return
        val play = musicService?.getCurrentPlaybackStatus() ?: return

        info.first?.let { binding.miniPlayerTitle.text = it }
        info.second?.let { binding.miniPlayerArtist.text = it }
        info.third?.let { binding.miniPlayerAlbum.setImageResource(it) }

        binding.miniSeekbar.max = play["duration"] as Int
        binding.miniSeekbar.progress = play["position"] as Int

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
