package com.example.umc_9th

import android.content.Context
import android.media.MediaPlayer
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.*

object MusicPlayerManager {

    private var mediaPlayer: MediaPlayer? = null
    var currentSong: Song? = null
        private set

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var playerJob: Job? = null

    // 1. 현재 재생 시간을 실시간으로 알려줌
    private val _playbackPosition = MutableStateFlow(0)
    val playbackPosition = _playbackPosition.asStateFlow()

    // 2. 현재 재생 중인 곡이 바뀌었는지 알려줌
    private val _currentSongFlow = MutableStateFlow<Song?>(null)
    val currentSongFlow = _currentSongFlow.asStateFlow()

    // 3. 재생 상태(재생/멈춤)를 알려줌
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow()

    fun loadSong(context: Context, song: Song) {
        mediaPlayer?.release() // 기존 음악이 있다면 해제

        currentSong = song
        _currentSongFlow.value = song
        _playbackPosition.value = song.second * 1000

        mediaPlayer = MediaPlayer.create(context, song.music).apply {
            seekTo(currentSong!!.second * 1000) // 저장된 위치로 이동
            setOnCompletionListener {
                // 노래가 끝나면
                stopPlayback()
            }
        }
    }

    fun play() {
        if (mediaPlayer != null && !isPlaying.value) {
            mediaPlayer?.start()
            _isPlaying.value = true
            startBroadcastingPosition()
        }
    }

    fun pause() {
        if (mediaPlayer != null && isPlaying.value) {
            mediaPlayer?.pause()
            _isPlaying.value = false
            stopBroadcastingPosition()
        }
    }

    fun seekTo(positionMs: Int) {
        mediaPlayer?.seekTo(positionMs)
        _playbackPosition.value = positionMs
    }

    private fun startBroadcastingPosition() {
        playerJob?.cancel()
        playerJob = scope.launch {
            while (_isPlaying.value) {
                _playbackPosition.value = mediaPlayer?.currentPosition ?: 0
                delay(500) // 0.5초마다 현재 위치 업데이트
            }
        }
    }

    private fun stopBroadcastingPosition() {
        playerJob?.cancel()
    }

    fun stopPlayback() {
        pause()
        mediaPlayer?.seekTo(0)
        _playbackPosition.value = 0
    }

    // 노래가 바뀔 때 현재 재생 시간 저장
    fun updateCurrentSecond() {
        currentSong?.second = (mediaPlayer?.currentPosition ?: 0) / 1000
    }
}