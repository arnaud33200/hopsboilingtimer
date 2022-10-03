package ca.arnaud.hopsboilingtimer.app.view

import android.Manifest
import android.os.Build
import androidx.compose.runtime.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NotificationPermissionDialog(
    onPermissionResult: (granted: Boolean) -> Unit,
) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        onPermissionResult(true)
        return
    }

    var permissionRequested by remember {
        mutableStateOf(false)
    }

    val notificationPermissionState = rememberPermissionState(
        permission = Manifest.permission.POST_NOTIFICATIONS,
        onPermissionResult = { permissionRequested = true }
    )
    val hasPermission = notificationPermissionState.status.isGranted

    LaunchedEffect(hasPermission, permissionRequested) {
        if (permissionRequested || hasPermission) {
            onPermissionResult(hasPermission)
        }

        if (notificationPermissionState.status.shouldShowRationale) {
            notificationPermissionState.launchPermissionRequest()
        } else {
            onPermissionResult(false)
        }
    }
}