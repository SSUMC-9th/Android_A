package com.example.umc_9th

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.os.Bundle
import android.os.IBinder
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import umc.study.umc_9th.R
import umc.study.umc_9th.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySongBinding
    lateinit var title : String
    lateinit var singer : String
    var repeat : Boolean = false
    var suffle : Boolean = false
    var playing : Boolean = false

    //서비스 정의
    private var musicService: MusicService? = null
    private var isBound = false
    private var updateJob: Job? = null

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MusicBinder
            musicService = binder.getService()
            isBound = true

            musicService?.let { service ->
                // SeekBar 최대값 설정
                val duration = service.getDuration()
                binding.progressBar.max = duration
                binding.lastTime.text = milliTotime(duration)

                // 현재 재생 상태 확인
                playing = service.isPlaying()

                // 현재 위치 업데이트
                val currentPos = service.getCurrentPosition()
                binding.progressBar.progress = currentPos
                binding.currentTime.text = milliTotime(currentPos)

                // SeekBar 업데이트 시작
                if (playing) {
                    startSeekBarUpdate()
                }

            }
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
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = intent.getStringExtra("title").toString()
        singer = intent.getStringExtra("singer").toString()
        binding.titleText.text = title
        binding.singerText.text = singer
        binding.repeatButton.setColorFilter(Color.rgb(140, 140, 140))
        binding.suffleButton.setColorFilter(Color.rgb(140, 140, 140))
        binding.backButton.setOnClickListener {
            finish()
        }
        binding.repeatButton.setOnClickListener {
            if(!repeat) binding.repeatButton.setColorFilter(Color.rgb(73, 6, 204))
            else binding.repeatButton.setColorFilter(Color.rgb(140, 140, 140))
            repeat = !repeat
        }
        binding.suffleButton.setOnClickListener {
            if(!suffle) binding.suffleButton.setColorFilter(Color.rgb(73,6,204))
            else binding.suffleButton.setColorFilter(Color.rgb(140,140,140))
            suffle = !suffle
        }

        val musicServiceIntent = Intent(this, MusicService::class.java).apply {
            putExtra("songTitle", title)
            putExtra("songArtist", singer)
            putExtra("isPlaying", false)  // 처음에는 일시정지 상태
        }
        ContextCompat.startForegroundService(this, musicServiceIntent)
        bindService(musicServiceIntent, connection, Context.BIND_AUTO_CREATE)

        musicService?.pauseMusic()
        binding.lastTime.text = milliTotime(musicService?.getDuration())
        //재생&멈춤 버튼 터치 시 MediaPlayer에 반영
        binding.playButton.setOnClickListener {
                if(playing) {
                    binding.playButton.setImageResource(R.drawable.btn_miniplay_mvplay)
                    musicService?.pauseMusic()
                    updateJob?.cancel()
                }
                else {
                    musicService?.playMusic()
                    startSeekBarUpdate()
                    binding.playButton.setImageResource(R.drawable.btn_miniplay_mvpause)
                }

                playing = !playing


        }

        //SeekBar 터치 시 MediaPlayer에 영
        binding.progressBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    musicService?.seekTo(progress)
                    binding.currentTime.text = milliTotime(progress)
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // SeekBar 터치 시 업데이트 일시 중지
                updateJob?.cancel()
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // SeekBar 터치 종료 시 업데이트 재개
                if (playing) {
                    startSeekBarUpdate()
                }
            }
        })
    }

    private fun startSeekBarUpdate() {
        updateJob?.cancel()
        updateJob = lifecycleScope.launch(Dispatchers.Main) {
            while (isActive && isBound && musicService?.isPlaying() == true) {
                    musicService?.let { service ->
                        val currentPosition = service.getCurrentPosition()
                        binding.progressBar.progress = currentPosition
                        binding.currentTime.text = milliTotime(currentPosition)
                    }
                    delay(100)  // 100ms마다 업데이트
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        unbindService(connection)
        isBound = false
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra(MainActivity.STRING_INTENT_KEY, title)
        }
        setResult(RESULT_OK, intent)

    }
    private fun milliTotime(milliseconds: Int?): String {
        val totalSeconds = milliseconds?.div(1000)
        val minutes = totalSeconds?.div(60)
        val seconds = totalSeconds?.rem(60)
        return String.format("%d:%02d", minutes, seconds)
    }
    override fun onPause() {
        super.onPause()
        updateJob?.cancel()
    }

    override fun onResume() {
        super.onResume()
        if (playing && isBound) {
            startSeekBarUpdate()
        }
    }
}