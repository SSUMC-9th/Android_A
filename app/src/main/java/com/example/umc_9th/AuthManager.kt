package com.example.umc_9th

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthManager private constructor(context: Context) {

    //private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val prefs: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    companion object {
        @Volatile
        private var instance: AuthManager? = null

        fun getInstance(context: Context): AuthManager {
            return instance ?: synchronized(this) {
                instance ?: AuthManager(context.applicationContext).also { instance = it }
            }
        }
    }

    /*
    // 회원가입
    fun signUp(
        email: String,
        password: String,
        onSuccess: (FirebaseUser) -> Unit,
        onFailure: (String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val user = result.user
                if (user != null) {
                    saveUserToPrefs(user.uid, user.email ?: "")
                    onSuccess(user)
                } else {
                    onFailure("회원가입 실패")
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception.message ?: "알 수 없는 오류")
            }
    }

    // 로그인
    fun signIn(
        email: String,
        password: String,
        onSuccess: (FirebaseUser) -> Unit,
        onFailure: (String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val user = result.user
                if (user != null) {
                    saveUserToPrefs(user.uid, user.email ?: "")
                    onSuccess(user)
                } else {
                    onFailure("로그인 실패")
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception.message ?: "알 수 없는 오류")
            }
    }
    // 현재 로그인된 사용자
    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }
     */

    // 로그아웃
    fun signOut() {
        prefs.edit().clear().apply()
    }

    // 로그인 상태 확인
    fun isLoggedIn(): Boolean {
        return prefs.getBoolean("is_logged_in", false)
    }

    // 🔥 Access Token 저장
    fun saveAccessToken(token: String) {
        prefs.edit().putString("access_token", token).apply()
    }

    // 🔥 Access Token 가져오기
    fun getAccessToken(): String? {
        return prefs.getString("access_token", null)
    }

    // 🔥 유저 정보 저장
    fun saveUserToPrefs(userId: String, email: String) {
        prefs.edit().apply {
            putString("user_id", userId)
            putString("user_email", email)
            putBoolean("is_logged_in", true)
            apply()
        }
    }

    // 🔥 유저 정보 가져오기
    fun getUserId(): String? {
        return prefs.getString("user_id", null)
    }

    fun getUserEmail(): String? {
        return prefs.getString("user_email", null)
    }
}