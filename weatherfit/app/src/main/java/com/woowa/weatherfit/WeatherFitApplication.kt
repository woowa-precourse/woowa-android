package com.woowa.weatherfit

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WeatherFitApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // 로컬 DB 동기화 제거 - 이제 모든 데이터는 서버에서 직접 가져옴
    }
}
