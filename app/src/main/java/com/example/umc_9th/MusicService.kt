package com.example.umc_9th

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import umc.study.umc_8th.R

class MusicService : Service() {

    private val CHANNEL_ID = "ForegroundMusicService"
    private val NOTI_ID = 713

    private var isPlayingState = false
    private var mediaPlayer: MediaPlayer? = null
    private val binder = MusicBinder()

    private val playlist = listOf(
        R.raw.next_level,
        R.raw.heya,
        R.raw.supernova
    )
    private var currentIndex = 0
    private var currentTitle = "제목"
    private var currentArtist = "가수"
    private var currentAlbumResId = R.drawable.img_album_exp

    inner class MusicBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()

        intent?.getStringExtra("songTitle")?.let { currentTitle = it }
        intent?.getStringExtra("songArtist")?.let { currentArtist = it }
        currentAlbumResId = intent?.getIntExtra("albumResId", currentAlbumResId) ?: currentAlbumResId

        val resId = when {
            currentTitle.contains("Next Level", true) -> R.raw.next_level
            currentTitle.contains("해야", true) -> R.raw.heya
            currentTitle.contains("Supernova", true) -> R.raw.supernova
            else -> R.raw.next_level
        }

        currentIndex = when (resId) {
            R.raw.next_level -> 0
            R.raw.heya -> 1
            R.raw.supernova -> 2
            else -> 0
        }

        if (mediaPlayer == null || mediaPlayer?.isPlaying == false) {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(this, resId)
            mediaPlayer?.setOnCompletionListener { playNext() }
        }

        startForeground(NOTI_ID, createNotification())

        if (intent?.getBooleanExtra("isPlaying", false) == true) {
            play()
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder = binder

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Music Service Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("음악 재생 중")
            .setContentText("$currentTitle - $currentArtist")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(true)
            .setOnlyAlertOnce(true)

        return notificationBuilder.build()
    }

    // ===== 외부에서 접근 가능한 함수들 =====
    fun play() {
        mediaPlayer?.start()
        isPlayingState = true
        startForeground(NOTI_ID, createNotification())
    }

    fun pause() {
        mediaPlayer?.pause()
        isPlayingState = false
        startForeground(NOTI_ID, createNotification())
    }

    fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
    }

    fun getDuration(): Int = mediaPlayer?.duration ?: 0

    fun getCurrentPosition(): Int = mediaPlayer?.currentPosition ?: 0

    fun isPlaying(): Boolean = isPlayingState

    fun playNext() {
        currentIndex = (currentIndex + 1) % playlist.size
        playCurrentSong()
    }

    fun playPrev() {
        currentIndex = if (currentIndex - 1 < 0) playlist.size - 1 else currentIndex - 1
        playCurrentSong()
    }

    private fun playCurrentSong() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null

        mediaPlayer = MediaPlayer.create(this, playlist[currentIndex])
        mediaPlayer?.setOnCompletionListener { playNext() }
        mediaPlayer?.start()
        isPlayingState = true
        startForeground(NOTI_ID, createNotification())
    }

    // ===== 상태 조회(추가) =====
    fun getCurrentSongInfo(): Triple<String?, String?, Int?> {
        return Triple(currentTitle, currentArtist, currentAlbumResId)
    }

    fun getCurrentPlaybackStatus(): Map<String, Any> {
        return mapOf(
            "duration" to (mediaPlayer?.duration ?: 0),
            "position" to (mediaPlayer?.currentPosition ?: 0),
            "isPlaying" to isPlayingState
        )
    }

    override fun onDestroy() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        super.onDestroy()
    }
}
