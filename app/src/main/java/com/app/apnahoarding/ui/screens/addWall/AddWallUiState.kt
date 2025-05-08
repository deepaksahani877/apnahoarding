package com.app.apnahoarding.ui.screens.addWall

import android.net.Uri


data class AddWallUiState(
    val length: String = "",
    val width: String = "",
    val price: String = "",
    val imageUris: List<Uri?> = listOf(null, null, null),
    val lengthError: String? = null,
    val widthError: String? = null,
    val priceError: String? = null,
    val imageError: Boolean = false
)
