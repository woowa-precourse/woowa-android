package com.woowa.weatherfit.data.remote.interceptor

import com.woowa.weatherfit.data.local.preferences.DeviceIdManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class DeviceIdInterceptor @Inject constructor(
    private val deviceIdManager: DeviceIdManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestWithDeviceId = originalRequest.newBuilder()
            .header(HEADER_DEVICE_ID, deviceIdManager.getDeviceId())
            .build()
        return chain.proceed(requestWithDeviceId)
    }

    companion object {
        private const val HEADER_DEVICE_ID = "X-DEVICE-ID"
    }
}
