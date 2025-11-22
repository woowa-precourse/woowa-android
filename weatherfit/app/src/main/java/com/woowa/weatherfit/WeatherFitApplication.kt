package com.woowa.weatherfit

import android.app.Application
import com.woowa.weatherfit.domain.usecase.cody.SyncCodiesUseCase
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class WeatherFitApplication : Application() {

    @Inject
    lateinit var syncCodiesUseCase: SyncCodiesUseCase

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()

        applicationScope.launch {
            try {
                syncCodiesUseCase()
            } catch (e: Exception) {
                // 동기화 실패 시 로그만 남기고 앱은 계속 실행
                e.printStackTrace()
            }
        }
    }
}
