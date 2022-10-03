package ca.arnaud.hopsboilingtimer.app.view

import android.Manifest
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import ca.arnaud.hopsboilingtimer.app.extension.launchNotificationPermissionSettings
import com.google.accompanist.permissions.ExperimentalPermissionsApi
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

    val notificationPermissionState = rememberPermissionState(
        permission = Manifest.permission.POST_NOTIFICATIONS,
        onPermissionResult = onPermissionResult
    )
    val context = LocalContext.current

    LaunchedEffect(notificationPermissionState) {

        if (!notificationPermissionState.status.shouldShowRationale) {
            notificationPermissionState.launchPermissionRequest()
        } else {
            onPermissionResult(false)
            context.launchNotificationPermissionSettings()
        }
    }
}