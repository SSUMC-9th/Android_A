package com.example.umc_9th

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import umc.study.umc_8th.BuildConfig

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // 🔥 BuildConfig에서 네이티브 앱 키 가져오기
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
    }
}