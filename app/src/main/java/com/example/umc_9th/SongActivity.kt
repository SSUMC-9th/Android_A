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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.umc_9th.data.firebase.FirebaseManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import umc.study.umc_8th.R

class SongActivity : AppCompatActivity() {

    private lateinit var firebaseManager: FirebaseManager
    private var isLiked = false
    private lateinit var btnLike: ImageButton

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

    private var currentSongId = 1
    private var currentTitle = ""
    private var currentArtist = ""
    private var currentAlbumResId = R.drawable.img_album_exp

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

        firebaseManager = FirebaseManager.getInstance()

        currentSongId = intent.getIntExtra("songId", 1)
        currentTitle = intent.getStringExtra("title") ?: "Unknown"
        currentArtist = intent.getStringExtra("artist") ?: "Unknown"
        currentAlbumResId = intent.getIntExtra("albumResId", R.drawable.img_album_exp)

        findViewById<TextView>(R.id.song_title).text = currentTitle
        findViewById<TextView>(R.id.song_artist).text = currentArtist
        findViewById<ImageView>(R.id.song_album_image).setImageResource(currentAlbumResId)


        btnRepeat = findViewById(R.id.btn_repeat)
        btnShuffle = findViewById(R.id.btn_shuffle)
        btnPlay = findViewById(R.id.btn_play)
        btnPrev = findViewById(R.id.btn_prev)
        btnNext = findViewById(R.id.btn_next)
        seekBar = findViewById(R.id.song_seekbar)
        currentTime = findViewById(R.id.current_time)
        btnLike = findViewById(R.id.btn_like)
        checkLikeStatus()

        val serviceIntent = Intent(this, MusicService::class.java).apply {
            putExtra("songTitle", currentTitle)
            putExtra("songArtist", currentArtist)
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

        btnLike.setOnClickListener {
            toggleLike()
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
            resultIntent.putExtra("songId", currentSongId)
            resultIntent.putExtra("title", currentTitle)
            resultIntent.putExtra("artist", currentArtist)
            resultIntent.putExtra("albumResId", currentAlbumResId)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }

    private fun checkLikeStatus() {
        firebaseManager.checkIfLiked(currentSongId) { liked ->
            runOnUiThread {
                isLiked = liked
                updateLikeButtonUI()
            }
        }
    }

    private fun toggleLike() {
        val newLikeStatus = !isLiked

        if (newLikeStatus) {
            val song = createCurrentSong()
            firebaseManager.addLikedSong(
                song = song,
                onSuccess = {
                    runOnUiThread {
                        isLiked = true
                        updateLikeButtonUI()
                        Toast.makeText(this, "좋아요 목록에 추가되었습니다", Toast.LENGTH_SHORT).show()
                    }
                },
                onFailure = { error ->
                    runOnUiThread {
                        Toast.makeText(this, "오류: $error", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        } else {
            firebaseManager.removeLikedSong(
                songId = currentSongId,
                onSuccess = {
                    runOnUiThread {
                        isLiked = false
                        updateLikeButtonUI()
                        Toast.makeText(this, "좋아요 목록에서 제거되었습니다", Toast.LENGTH_SHORT).show()
                    }
                },
                onFailure = { error ->
                    runOnUiThread {
                        Toast.makeText(this, "오류: $error", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }
    }

    private fun createCurrentSong(): Song {
        return Song(
            id = currentSongId,
            title = currentTitle,
            singer = currentArtist,
            second = 0,
            playTime = 180000,
            isPlaying = false,
            music = "music_${currentTitle.lowercase()}",
            coverImg = currentAlbumResId,
            isLike = true,
            albumIdx = 1
        )
    }

    private fun updateLikeButtonUI() {
        if (isLiked) {
            btnLike.setImageResource(R.drawable.ic_my_like_on)
        } else {
            btnLike.setImageResource(R.drawable.ic_my_like_off)
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
