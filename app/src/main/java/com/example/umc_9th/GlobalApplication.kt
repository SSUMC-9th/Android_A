package com.example.umc_9th

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, "42efdf23528e21d70ba7125ea564fde3")
    }
}