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

    //채널 ID와 Notificiation ID 설정
    private val CHANNEL_ID = "ForegroundMusicService"
    private val NOTI_ID = 713

    private var mediaPlayer: MediaPlayer? = null
    private val binder = MusicBinder()

    private val playlist = listOf(
        R.raw.next_level,
        R.raw.heya,
        R.raw.supernova
    )
    private var currentIndex = 0

    //현재 재생 중인 노래 정보를 저장할 변수
    private var currentSongTitle: String = "Unknown Title"
    private var currentSongArtist: String = "Unknown Artist"

    //Activity에 Service 인스턴스를 전달
    inner class MusicBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    //서비스 시작 시 초기화
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        //Notification 활성화
        createNotificationChannel()

        val title = intent?.getStringExtra("songTitle") ?: "Unknown"
        val artist = intent?.getStringExtra("songArtist") ?: "Unknown"
        val isPlaying = intent?.getBooleanExtra("isPlaying", false) ?: false



        // 제목/가수에 맞는 음원 파일 할당
        val resId = when {
            title.contains("Next Level", true) -> R.raw.next_level
            title.contains("해야", true) -> R.raw.heya
            title.contains("Supernova", true) -> R.raw.supernova
            else -> R.raw.next_level
        }

        currentIndex = when (resId) {
            R.raw.next_level -> 0
            R.raw.heya -> 1
            R.raw.supernova -> 2
            else -> 0
        }

        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(this, resId).apply {
            setOnCompletionListener { playNext() } // 끝나면 다음 곡 자동 재생
        }

        currentSongTitle = title
        currentSongArtist = artist

        if (isPlaying) mediaPlayer?.start()

        //알람도 같이 설정
        val notification = createNotification()
        startForeground(NOTI_ID, notification)

        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    //알람 채널 설정(CHANNEL_ID와 name은 자유롭게 설정!)
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

    //알람으로 노래가 재생중임을 표시
    private fun createNotification(): Notification {
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("음악 재생 중")
            .setContentText("$currentSongTitle - $currentSongArtist")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(true)
            .setOnlyAlertOnce(true)

        return notificationBuilder.build()
    }

    //외부에서 서비스에 접근 가능한 함수들
    //음악 재생
    fun play() {
        mediaPlayer?.start()
    }
    //음악 멈춤
    fun pause() {
        mediaPlayer?.pause()
    }
    //인자로 받은 위치로 곡의 재생 위치 이동
    fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
    }
    //현재 재생중인 노래의 길이 리턴
    fun getDuration(): Int {
        return mediaPlayer?.duration ?: 0
    }
    //현재 노래의 위치 리턴(SeekBar에 넣을 거)
    fun getCurrentPosition(): Int {
        return mediaPlayer?.currentPosition ?: 0
    }
    //재생 중임?
    fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying ?: false
    }
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
        mediaPlayer?.start()
    }

    fun updateCurrentSongInfo(title: String, artist: String) {
        currentSongTitle = title
        currentSongArtist = artist
        val notification = createNotification()
        startForeground(NOTI_ID, notification)
    }

    override fun onDestroy() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        super.onDestroy()
    }
}