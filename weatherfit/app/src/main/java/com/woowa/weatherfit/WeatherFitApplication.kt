package com.woowa.weatherfit

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class WeatherFitApplication : Application(), ImageLoaderFactory {

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onCreate() {
        super.onCreate()
        // 로컬 DB 동기화 제거 - 이제 모든 데이터는 서버에서 직접 가져옴
    }

    override fun newImageLoader(): ImageLoader {
        return imageLoader
    }
}
