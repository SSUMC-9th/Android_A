package com.example.umc_9th

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import umc.study.umc_9th.BuildConfig

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_API)
    }
}