package com.example.umc_9th

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import umc.study.umc_8th.R

class SongActivity : AppCompatActivity() {
    private var isRepeatOn = false
    private var isShuffleOn = false
    private var isPlayOn = false

    private var musicService: MusicService? = null
    private var isBound = false
    private var updateJob: Job? = null

    private lateinit var btnRepeat: ImageButton
    private lateinit var btnShuffle: ImageButton
    private lateinit var btnPlay: ImageButton
    private lateinit var btnPrev: ImageButton
    private lateinit var btnNext: ImageButton
    private lateinit var seekBar: SeekBar
    private lateinit var currentTime: TextView

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MusicBinder
            musicService = binder.getService()
            isBound = true

            // 서비스 상태로 즉시 동기화 (일시정지여도 위치/아이콘 반영)
            val playbackStatus = musicService!!.getCurrentPlaybackStatus()
            val duration = playbackStatus["duration"] as Int
            val position = playbackStatus["position"] as Int
            val playing = playbackStatus["isPlaying"] as Boolean

            seekBar.max = duration
            seekBar.progress = position
            findViewById<TextView>(R.id.total_time).text = formatTime(duration)
            currentTime.text = formatTime(position)

            if (playing) {
                btnPlay.setImageResource(R.drawable.nugu_btn_pause_32)
                isPlayOn = true
                updateSeekBar()
            } else {
                btnPlay.setImageResource(R.drawable.nugu_btn_play_32)
                isPlayOn = false
            }
        }
        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song)

        val title = intent.getStringExtra("title")
        val artist = intent.getStringExtra("artist")
        val albumResId = intent.getIntExtra("albumResId", R.drawable.btn_textbox_close)
        findViewById<TextView>(R.id.song_title).text = title
        findViewById<TextView>(R.id.song_artist).text = artist
        findViewById<ImageView>(R.id.song_album_image).setImageResource(albumResId)

        btnRepeat = findViewById(R.id.btn_repeat)
        btnShuffle = findViewById(R.id.btn_shuffle)
        btnPlay = findViewById(R.id.btn_play)
        btnPrev = findViewById(R.id.btn_prev)
        btnNext = findViewById(R.id.btn_next)
        seekBar = findViewById(R.id.song_seekbar)
        currentTime = findViewById(R.id.current_time)

        val serviceIntent = Intent(this, MusicService::class.java).apply {
            putExtra("songTitle", title ?: "Unknown")
            putExtra("songArtist", artist ?: "Unknown")
            putExtra("isPlaying", false)
        }
        ContextCompat.startForegroundService(this, serviceIntent)
        bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE)

        btnRepeat.setOnClickListener {
            isRepeatOn = !isRepeatOn
            if (isRepeatOn) {
                btnRepeat.setImageResource(R.drawable.custom_refresh_black)
            } else {
                btnRepeat.setImageResource(R.drawable.custom_refresh_gray)
            }
        }

        btnShuffle.setOnClickListener {
            isShuffleOn = !isShuffleOn
            if (isShuffleOn) {
                btnShuffle.setImageResource(R.drawable.custom_arrows_shuffle_black)
            } else {
                btnShuffle.setImageResource(R.drawable.custom_arrows_shuffle_gray)
            }
        }

        btnPlay.setOnClickListener {
            isPlayOn = !isPlayOn
            if (isPlayOn) {
                btnPlay.setImageResource(R.drawable.nugu_btn_pause_32)
                musicService?.play()
                updateSeekBar()
            } else {
                btnPlay.setImageResource(R.drawable.nugu_btn_play_32)
                musicService?.pause()
                // 일시정지 시에도 현재 위치/시간 유지 표시
                val pos = musicService?.getCurrentPosition() ?: 0
                seekBar.progress = pos
                currentTime.text = formatTime(pos)
            }
        }

        btnNext.setOnClickListener {
            musicService?.playNext()
            btnPlay.setImageResource(R.drawable.nugu_btn_pause_32)
            isPlayOn = true
            updateSeekBar()
        }
        btnPrev.setOnClickListener {
            musicService?.playPrev()
            btnPlay.setImageResource(R.drawable.nugu_btn_pause_32)
            isPlayOn = true
            updateSeekBar()
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) musicService?.seekTo(progress)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        findViewById<ImageButton>(R.id.song_drop_button).setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("title", title)
            resultIntent.putExtra("artist", artist)
            resultIntent.putExtra("albumResId", albumResId)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }

    private fun updateSeekBar() {
        updateJob?.cancel()
        updateJob = lifecycleScope.launch(Dispatchers.Main) {
            while (isBound && musicService?.isPlaying() == true) {
                val pos = musicService!!.getCurrentPosition()
                seekBar.progress = pos
                currentTime.text = formatTime(pos)
                delay(500)
            }
        }
    }

    private fun formatTime(ms: Int): String {
        val totalSec = ms / 1000
        val min = totalSec / 60
        val sec = totalSec % 60
        return String.format("%02d:%02d", min, sec)
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
