package com.example.umc_9th

import android.content.Context
import android.content.SharedPreferences

object Prefs {
    private const val PREFS_NAME = "auth_prefs" // 수첩 이름
    private const val KEY_TOKEN = "accessToken" // 토큰을 저장할 페이지 이름

    // 1. 저장하기 (Set)
    fun setToken(context: Context, token: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString(KEY_TOKEN, token) // (Key, Value)
        editor.apply() // ⭐ 저장 확정! (apply는 비동기로 저장해서 빠름)
    }

    // 2. 가져오기 (Get)
    fun getToken(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        // 저장된 게 없으면 빈 문자열("") 반환
        return prefs.getString(KEY_TOKEN, "") ?: ""
    }

    // 3. 삭제하기 (Logout)
    fun clearToken(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.clear() // 모든 데이터 삭제
        editor.apply()
    }
}