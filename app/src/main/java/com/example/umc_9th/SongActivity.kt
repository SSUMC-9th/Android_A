package com.example.umc_9th

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
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
            initSeekBar()

            // 현재 재생 중이면 UI 갱신
            if (musicService?.isPlaying() == true) {
                btnPlay.setImageResource(R.drawable.nugu_btn_pause_32)
                isPlayOn = true
                updateSeekBar()
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

        // 서비스 시작 및 바인딩
        val serviceIntent = Intent(this, MusicService::class.java).apply {
            putExtra("songTitle", title ?: "Unknown")
            putExtra("songArtist", artist ?: "Unknown")
            putExtra("isPlaying", false)
        }
        ContextCompat.startForegroundService(this, serviceIntent)
        bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE)

        // 반복 버튼 클릭 이벤트
        btnRepeat.setOnClickListener {
            isRepeatOn = !isRepeatOn
            if (isRepeatOn) {
                btnRepeat.setImageResource(R.drawable.custom_refresh_black)
            } else {
                btnRepeat.setImageResource(R.drawable.custom_refresh_gray)
            }
        }

        // 전체재생(셔플) 버튼 클릭 이벤트
        btnShuffle.setOnClickListener {
            isShuffleOn = !isShuffleOn
            if (isShuffleOn) {
                btnShuffle.setImageResource(R.drawable.custom_arrows_shuffle_black)
            } else {
                btnShuffle.setImageResource(R.drawable.custom_arrows_shuffle_gray)
            }
        }

        // 재생 버튼 클릭 이벤트
        btnPlay.setOnClickListener {
            isPlayOn = !isPlayOn
            if (isPlayOn) {
                btnPlay.setImageResource(R.drawable.nugu_btn_pause_32)
                musicService?.play()
            } else {
                btnPlay.setImageResource(R.drawable.nugu_btn_play_32)
                musicService?.pause()
            }
            updateSeekBar()
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

    private fun initSeekBar() {
        val duration = musicService?.getDuration() ?: 0
        seekBar.max = duration
        findViewById<TextView>(R.id.total_time).text = formatTime(duration)
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
    }
}

