package com.example.umc_9th

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import umc.study.umc_9th.R

class MusicService : Service() {

    private val CHANNEL_ID = "ForegroundMusicService"
    private val NOTI_ID = 713

    private var mediaPlayer: MediaPlayer? = null
    private val binder = MusicBinder()

    private var currentSongTitle: String = "Unknown Title"
    private var currentSongArtist: String = "Unknown Artist"
    private var isCurrentlyPlaying: Boolean = false

    inner class MusicBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    @SuppressLint("ForegroundServiceType")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()

        val initialTitle = intent?.getStringExtra("songTitle") ?: "Unknown Title"
        val initialArtist = intent?.getStringExtra("songArtist") ?: "Unknown Artist"
        val shouldPlay = intent?.getBooleanExtra("isPlaying", false) ?: false

        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.sample_music_a)
            mediaPlayer?.setOnCompletionListener {
                isCurrentlyPlaying = false
                updateNotification()
            }
            currentSongTitle = initialTitle
            currentSongArtist = initialArtist
            if (shouldPlay) {
                playMusic()
            }

        }

        val notification = createNotification()
        startForeground(NOTI_ID, notification)

        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Foreground Music Service Channel",
            NotificationManager.IMPORTANCE_LOW
        )
        channel.setShowBadge(false)
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    private fun createNotification(): Notification {
        val statusText = if (isCurrentlyPlaying) "재생 중" else "일시정지"

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(currentSongTitle)
            .setContentText("$currentSongArtist - $statusText")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setShowWhen(false)
            .build()
    }

    private fun updateNotification() {
        val notification = createNotification()
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.notify(NOTI_ID, notification)
    }

    fun playMusic() {

        mediaPlayer?.let {
            if (!it.isPlaying) {
                it.start()
                isCurrentlyPlaying = true
                updateNotification()
            }
        }
    }
    fun pauseMusic() {

        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
                isCurrentlyPlaying = false
                updateNotification()
            }
        }
    }
    fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
    }

    fun updateCurrentSongInfo(title: String, artist: String) {
        currentSongTitle = title
        currentSongArtist = artist
        updateNotification()
    }

    fun getDuration(): Int {
        return mediaPlayer?.duration ?: 0
    }

    fun getCurrentPosition(): Int {
        return mediaPlayer?.currentPosition ?: 0
    }

    fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying ?: false
    }
    override fun onDestroy() {

        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null

        super.onDestroy()
    }
}
