package com.example.cam_map

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.jar.Manifest

object PermissionHelper {
    fun askForLocationPermission(activity: Activity) {
        ActivityCompat.requestPermissions(activity, PERMISSIONS, REQUEST_CODE_FOR_PERMISSION)
    }

    fun checkPermissionsGranted(context: Context) =
        PERMISSIONS.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

    const val REQUEST_CODE_FOR_PERMISSION = 0x1001
    private val PERMISSIONS = arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION)
}