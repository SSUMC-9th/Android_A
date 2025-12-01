package com.example.umc_9th.util

import android.content.Context
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend fun UserApiClient.Companion.loginWithKakao(context: Context): OAuthToken {
    return suspendCoroutine { continuation ->

        instance.loginWithKakaoAccount(context) { token, error ->
            if (error != null) {
                continuation.resumeWithException(error)
            } else if (token != null) {
                continuation.resume(token)
            }
        }
    }
}