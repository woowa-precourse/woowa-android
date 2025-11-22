package com.woowa.weatherfit.data.remote.interceptor

import com.woowa.weatherfit.data.local.DeviceIdDataStore
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class DeviceIdInterceptor @Inject constructor(
    private val deviceIdDataStore: DeviceIdDataStore
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val deviceId = runBlocking {
            deviceIdDataStore.getOrCreateDeviceId()
        }

        val request = chain.request().newBuilder()
            .addHeader("X-DEVICE-ID", deviceId)
            .build()

        return chain.proceed(request)
    }
}
