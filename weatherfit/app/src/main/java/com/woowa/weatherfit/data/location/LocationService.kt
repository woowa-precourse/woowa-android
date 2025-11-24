package com.woowa.weatherfit.data.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

data class LocationData(
    val latitude: Double,
    val longitude: Double
)

class LocationService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    suspend fun getCurrentLocation(): Result<LocationData> {
        Log.d(TAG, "getCurrentLocation() called")

        if (!hasLocationPermission()) {
            Log.e(TAG, "Location permission not granted")
            return Result.failure(SecurityException("Location permission not granted"))
        }

        return try {
            val location = getLastKnownLocation()
            if (location != null) {
                Log.d(TAG, "GPS Location received - Latitude: ${location.latitude}, Longitude: ${location.longitude}")
                Log.d(TAG, "Location accuracy: ${location.accuracy}m, Provider: ${location.provider}")
                Result.success(LocationData(location.latitude, location.longitude))
            } else {
                Log.e(TAG, "Unable to get current location - location is null")
                Result.failure(Exception("Unable to get current location"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception while getting location: ${e.message}", e)
            Result.failure(e)
        }
    }

    companion object {
        private const val TAG = "LocationService"
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    private suspend fun getLastKnownLocation(): Location? = suspendCancellableCoroutine { continuation ->
        try {
            val cancellationTokenSource = CancellationTokenSource()
            Log.d(TAG, "Requesting GPS location with PRIORITY_HIGH_ACCURACY")

            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource.token
            ).addOnSuccessListener { location ->
                if (location != null) {
                    Log.d(TAG, "FusedLocationProvider success: lat=${location.latitude}, lon=${location.longitude}")
                } else {
                    Log.w(TAG, "FusedLocationProvider returned null location")
                }
                continuation.resume(location)
            }.addOnFailureListener { exception ->
                Log.e(TAG, "FusedLocationProvider failed: ${exception.message}", exception)
                continuation.resume(null)
            }

            continuation.invokeOnCancellation {
                Log.d(TAG, "Location request cancelled")
                cancellationTokenSource.cancel()
            }
        } catch (e: SecurityException) {
            Log.e(TAG, "SecurityException in getLastKnownLocation: ${e.message}", e)
            continuation.resume(null)
        }
    }
}
