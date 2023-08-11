package ca.arnaud.hopsboilingtimer.app.extension

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.annotation.RawRes

// TODO - doesn't seem to work
fun Context.rawResToUri(@RawRes id: Int): Uri {
    return Uri.Builder().apply {
        // output: android.resource://ca.arnaud.hopsboilingtimer/raw/schedule_add_now
        scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
        appendEncodedPath("/${resources.getResourcePackageName(id)}")
        appendEncodedPath(resources.getResourceTypeName(id))
        appendEncodedPath(resources.getResourceEntryName(id))
    }.build()
}