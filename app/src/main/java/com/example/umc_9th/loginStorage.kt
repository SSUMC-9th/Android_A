package com.example.umc_9th

import android.content.Context

class loginStorage(context: Context) {
    private val preferences = context.getSharedPreferences("uid",
        Context.MODE_PRIVATE)

    companion object {
        private const val uid = "uid"
    }

    fun getUid(): String? {
        return preferences.getString(uid, "")
    }

    fun setUid(_uid : String) {
        preferences.edit().putString(uid, _uid).apply()
    }
}