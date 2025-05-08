package com.app.apnahoarding.utils


import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.runtime.MutableState
import androidx.core.content.FileProvider
import java.io.File

fun launchCamera(
    context: Context,
    cameraImageUri: MutableState<Uri?>,
    cameraLauncher: ManagedActivityResultLauncher<Uri, Boolean>
) {
    val uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "image_${System.currentTimeMillis()}.jpg"
        )
    )
    cameraImageUri.value = uri
    cameraLauncher.launch(uri)
}
