package com.example.umc_9th

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.umc_9th.entitiy.SongTableEntity
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.*
import umc.study.umc_9th.R
import umc.study.umc_9th.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {

    private val uid: String = loginStorage(this).getUid().toString()
    private val userSongsRef = FirebaseDatabase.getInstance().getReference(uid).child("songs")

    private lateinit var binding: ActivitySongBinding
    private var songID: Int = 0
    private var startPos = 0
    private var maxPos = 100
    private var repeat = false
    private var shuffle = false
    private var playing = false

    private lateinit var thisContext: Context

    private var musicService: MusicService? = null
    private var isBound = false
    private var updateJob: Job? = null

    private val songList = mutableListOf<SongTableEntity>()

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MusicBinder
            musicService = binder.getService()
            isBound = true

            binding.progressBar.max = maxPos
            binding.progressBar.progress = startPos
            musicService?.seekTo(binding.progressBar.progress)
            binding.lastTime.text = milliToTime(binding.progressBar.max)
            binding.currentTime.text = milliToTime(binding.progressBar.progress)

            if (playing) startSeekBarUpdate()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
            musicService = null
            updateJob?.cancel()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        thisContext = this

        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = intent.getStringExtra("title") ?: ""
        val singer = intent.getStringExtra("singer") ?: ""
        songID = intent.getIntExtra("songID", 0)
        startPos = intent.getIntExtra("seekBarPr", 0)
        maxPos = intent.getIntExtra("seekBarMax", 100)

        binding.titleText.text = title
        binding.singerText.text = singer

        binding.repeatButton.setColorFilter(Color.rgb(140, 140, 140))
        binding.suffleButton.setColorFilter(Color.rgb(140, 140, 140))

        loadSongsFromFirebase()
        setupMusicService(title, singer)
        setupButtons()
        setupSeekBar()
    }

    private fun loadSongsFromFirebase() {
        userSongsRef.get()
            .addOnSuccessListener { dataSnapshot ->
                songList.clear()
                for (child in dataSnapshot.children) {
                    val song = child.getValue(SongTableEntity::class.java)
                    if (song != null) songList.add(song)
                }

                Log.d("Firebase", "노래 ${songList.size}개 로드 성공")

                val currentSong = songList.firstOrNull { it.id == songID }
                currentSong?.let { song ->
                    lifecycleScope.launch(Dispatchers.Main) {
                        binding.titleText.text = song.title
                        binding.singerText.text = song.singer
                        binding.likeButton.setImageResource(
                            if (song.isLike) R.drawable.ic_my_like_on else R.drawable.ic_my_like_off
                        )
                    }
                }
            }
            .addOnFailureListener {
                Log.e("Firebase", "데이터 로드 실패: ${it.message}")
            }
    }

    private fun setupMusicService(title: String, singer: String) {
        val musicServiceIntent = Intent(this, MusicService::class.java).apply {
            putExtra("songTitle", title)
            putExtra("songArtist", singer)
            putExtra("isPlaying", false)
        }

        ContextCompat.startForegroundService(this, musicServiceIntent)
        bindService(musicServiceIntent, connection, Context.BIND_AUTO_CREATE)
    }

    private fun setupSeekBar() {
        binding.progressBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser && isBound) {
                    musicService?.seekTo(progress)
                    binding.currentTime.text = milliToTime(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                updateJob?.cancel()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (playing) startSeekBarUpdate()
            }
        })
    }

    private fun setupButtons() {
        binding.backButton.setOnClickListener {
            val resultIntent = Intent().apply {
                putExtra("seekBarPr", binding.progressBar.progress)
                putExtra("seekBarMax", binding.progressBar.max)
                putExtra("title", binding.titleText.text.toString())
                putExtra("songID", songID)
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }

        binding.repeatButton.setOnClickListener {
            repeat = !repeat
            val color = if (repeat) Color.rgb(73, 6, 204) else Color.rgb(140, 140, 140)
            binding.repeatButton.setColorFilter(color)
        }

        binding.suffleButton.setOnClickListener {
            shuffle = !shuffle
            val color = if (shuffle) Color.rgb(73, 6, 204) else Color.rgb(140, 140, 140)
            binding.suffleButton.setColorFilter(color)
        }

        binding.playButton.setOnClickListener {
            if (playing) {
                musicService?.pauseMusic()
                updateJob?.cancel()
                binding.playButton.setImageResource(R.drawable.btn_miniplay_mvplay)
            } else {
                musicService?.playMusic()
                startSeekBarUpdate()
                binding.playButton.setImageResource(R.drawable.btn_miniplay_mvpause)
            }
            playing = !playing
        }

        binding.likeButton.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val targetSong = songList.firstOrNull { it.id == songID } ?: return@launch
                targetSong.isLike = !targetSong.isLike

                withContext(Dispatchers.Main) {
                    binding.likeButton.setImageResource(
                        if (targetSong.isLike) R.drawable.ic_my_like_on
                        else R.drawable.ic_my_like_off
                    )
                }

                userSongsRef.child(targetSong.id.toString()).setValue(targetSong)
            }
        }

        binding.back.setOnClickListener {
            moveToSong(-1)
        }

        binding.forward.setOnClickListener {
            moveToSong(+1)
        }
    }

    private fun moveToSong(direction: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            val currentIndex = songList.indexOfFirst { it.id == songID }
            if (currentIndex == -1) return@launch

            val newIndex = (currentIndex + direction).coerceIn(0, songList.lastIndex)
            val nextSong = songList.getOrNull(newIndex) ?: return@launch

            songID = nextSong.id
            currentSongStorage(thisContext).setCurrentShow(songID)

            withContext(Dispatchers.Main) {
                binding.titleText.text = nextSong.title
                binding.singerText.text = nextSong.singer
                binding.likeButton.setImageResource(
                    if (nextSong.isLike) R.drawable.ic_my_like_on else R.drawable.ic_my_like_off
                )
            }
        }
    }

    private fun startSeekBarUpdate() {
        updateJob?.cancel()
        updateJob = lifecycleScope.launch(Dispatchers.Main) {
            while (isActive && isBound && musicService?.isPlaying() == true) {
                musicService?.let { service ->
                    val pos = service.getCurrentPosition()
                    binding.progressBar.progress = pos
                    binding.currentTime.text = milliToTime(pos)
                }
                delay(100)
            }
        }
    }

    private fun milliToTime(milliseconds: Int?): String {
        if (milliseconds == null) return "0:00"
        val totalSeconds = milliseconds / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%d:%02d", minutes, seconds)
    }

    override fun onPause() {
        super.onPause()
        updateJob?.cancel()
    }

    override fun onResume() {
        super.onResume()
        if (playing && isBound) startSeekBarUpdate()
    }

    override fun onDestroy() {
        super.onDestroy()
        updateJob?.cancel()
        if (isBound) {
            unbindService(connection)
            isBound = false
        }
    }
}
