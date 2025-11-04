//package com.example.umc_9th
//
//import android.media.MediaPlayer
//import android.os.Bundle
//import android.view.View
//import android.widget.SeekBar
//import androidx.appcompat.app.AppCompatActivity
//import kotlinx.coroutines.*
//import umc.study.umc_8th.databinding.ActivitySongBinding
//import java.text.SimpleDateFormat
//import java.util.*
//
//class SongActivity : AppCompatActivity() {
//
//    lateinit var binding: ActivitySongBinding
//    private var song: Song? = null
//
//    private var mediaPlayer: MediaPlayer? = null
//    private var playerJob: Job? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivitySongBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        initSongAndPlayer()
//
//        binding.songPreviousIv.setOnClickListener {
//            restartSong()
//        }
//        binding.songNextIv.setOnClickListener {
//            restartSong()
//        }
//        binding.songMiniplayerIv.setOnClickListener {
//            setPlayerStatus(true)
//            mediaPlayer?.start()
//        }
//        binding.songPauseIv.setOnClickListener {
//            setPlayerStatus(false)
//            mediaPlayer?.pause()
//        }
//
//        binding.songDownIb.setOnClickListener { finish() }
//    }
//
//    private fun initSongAndPlayer() {
//        if (intent.hasExtra("title") && intent.hasExtra("singer")) {
//            song = Song(
//                intent.getStringExtra("title")!!,
//                intent.getStringExtra("singer")!!,
//                intent.getIntExtra("second", 0),
//                intent.getIntExtra("playtime", 60),
//                intent.getBooleanExtra("isPlaying", false),
//                intent.getIntExtra("music", 0)
//            )
//
//            // MediaPlayer 생성
//            mediaPlayer = MediaPlayer.create(this, song!!.music)
//
//            setPlayerUI(song!!)
//
//        } else {
//            finish()
//        }
//    }
//
//    private fun setPlayerUI(currentSong: Song) {
//        binding.songMusicTitleTv.text = currentSong.title
//        binding.songSingerNameTv.text = currentSong.singer
//        binding.songStartTimeTv.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(currentSong.second * 1000)
//
//        val duration = mediaPlayer?.duration ?: 0
//        binding.songEndTimeTv.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(duration)
//
//        // SeekBar의 최대값도 실제 음악 길이로 설정
//        binding.songProgressbarSb.max = duration
//        binding.songProgressbarSb.progress = currentSong.second * 1000
//
//        setPlayerStatus(currentSong.isPlaying)
//    }
//
//    private fun setPlayerStatus(isPlaying: Boolean) {
//        song?.isPlaying = isPlaying
//
//        if (isPlaying) {
//            binding.songMiniplayerIv.visibility = View.GONE
//            binding.songPauseIv.visibility = View.VISIBLE
//            startTimer() // 재생 시 타이머 시작
//        } else {
//            binding.songMiniplayerIv.visibility = View.VISIBLE
//            binding.songPauseIv.visibility = View.GONE
//            playerJob?.cancel() // 멈춤 시 타이머 중지
//        }
//    }
//
//    private fun restartSong() {
//        mediaPlayer?.seekTo(0)
//        mediaPlayer?.start()
//        setPlayerStatus(true)
//    }
//    private fun startTimer() {
//        playerJob?.cancel() // 기존 Job이 있다면 취소
//
//        playerJob = CoroutineScope(Dispatchers.Default).launch {
//            while (true) {
//                // 재생 중일 때만 UI 업데이트
//                if (mediaPlayer?.isPlaying == true) {
//                    val currentPosition = mediaPlayer?.currentPosition ?: 0
//                    // UI 업데이트는 Main 스레드에서 실행
//                    withContext(Dispatchers.Main) {
//                        binding.songStartTimeTv.text =
//                            SimpleDateFormat("mm:ss", Locale.getDefault()).format(currentPosition)
//                        binding.songProgressbarSb.progress = currentPosition
//                    }
//                }
//                delay(500) // 0.5초 간격으로 체크
//            }
//        }
//    }
//
//    // 생명주기에 맞춰 MediaPlayer 리소스를 관리합니다.
//    override fun onPause() {
//        super.onPause()
//        // 화면이 보이지 않을 때 음악을 일시정지하고 상태를 저장
//        mediaPlayer?.pause()
//        setPlayerStatus(false)
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        playerJob?.cancel()     // 코루틴 종료
//        mediaPlayer?.release()  // MediaPlayer 자원 해제
//        mediaPlayer = null      // 메모리 누수 방지
//    }
//}


package com.example.umc_9th

import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import umc.study.umc_8th.databinding.ActivitySongBinding
import java.text.SimpleDateFormat
import java.util.*

class SongActivity : AppCompatActivity() {

    lateinit var binding: ActivitySongBinding
    private var song: Song? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSongAndPlayer() // UI 초기화
        observePlayerState() // 관리자 상태 관찰 시작

        // 모든 버튼이 MusicPlayerManager를 제어하도록
        binding.songMiniplayerIv.setOnClickListener {
            MusicPlayerManager.play()
        }
        binding.songPauseIv.setOnClickListener {
            MusicPlayerManager.pause()
        }
        binding.songPreviousIv.setOnClickListener {
            MusicPlayerManager.stopPlayback()
            MusicPlayerManager.play()
        }
        binding.songNextIv.setOnClickListener {
            MusicPlayerManager.stopPlayback()
            MusicPlayerManager.play()
        }
        binding.songDownIb.setOnClickListener {
            finish()
        }

        // SeekBar 리스너
        binding.songProgressbarSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    binding.songStartTimeTv.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(progress)
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seekBar?.progress?.let {
                    MusicPlayerManager.seekTo(it) // 사용자가 터치를 떼면 관리자에게 알림
                }
            }
        })
    }

    private fun initSongAndPlayer() {
        // 관리자로부터 현재 곡 정보를 가져옴
        this.song = MusicPlayerManager.currentSong

        if (song == null) {
            finish() // 재생 중인 곡이 없으면 액티비티 종료
            return
        }
        setPlayerUI(song!!)
    }

    private fun setPlayerUI(currentSong: Song) {
        binding.songMusicTitleTv.text = currentSong.title
        binding.songSingerNameTv.text = currentSong.singer

        val duration = currentSong.playtime * 1000
        binding.songEndTimeTv.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(duration)
        binding.songProgressbarSb.max = duration
    }

    private fun observePlayerState() {
        lifecycleScope.launch {
            // 1. 재생 상태(재생/멈춤)가 바뀌면 버튼 아이콘 업데이트
            MusicPlayerManager.isPlaying.collect { isPlaying ->
                if (isPlaying) {
                    binding.songMiniplayerIv.visibility = View.GONE
                    binding.songPauseIv.visibility = View.VISIBLE
                } else {
                    binding.songMiniplayerIv.visibility = View.VISIBLE
                    binding.songPauseIv.visibility = View.GONE
                }
            }
        }

        lifecycleScope.launch {
            // 2. 재생 시간이 바뀌면 SeekBar와 시간 텍스트 업데이트
            MusicPlayerManager.playbackPosition.collect { position ->
                binding.songProgressbarSb.progress = position
                binding.songStartTimeTv.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(position)

                // 현재 재생 시간을 song 객체에 저장
                song?.second = position / 1000
            }
        }
    }

    override fun onPause() {
        super.onPause()
        MusicPlayerManager.updateCurrentSecond()
    }
}
