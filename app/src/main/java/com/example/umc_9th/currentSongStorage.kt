package com.example.umc_9th

import android.content.Context

class currentSongStorage(context : Context) {
    private val preferences = context.getSharedPreferences("current_songID",
        Context.MODE_PRIVATE)

    //컴패니언 오브젝트로 TutorialStorage 클래스 외에서는 조작 가
    companion object {
        // SharedPreferences 키를 상수로 정의
        private val CurrentSong = "CurrentSongID"
    }

    fun getCurrentSong(): Int {
        //TUTORI    AL_SHOWN 키의 값을 리턴
        //값이 없으면 기본값인 false를 리턴
        return preferences.getInt(CurrentSong, 0)
    }

    fun setCurrentShow(ID : Int) {
        //데이터를 수정하기 위해 editor를 얻고, 값 삽입
        //apply()를 호출하여 저장
        preferences.edit().putInt(CurrentSong, ID).apply()
    }
}