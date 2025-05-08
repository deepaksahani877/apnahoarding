package com.app.apnahoarding.ui.screens.imageUpload

data class UploadImageUiState(
    val addressLine: String = "",
    val city: String = "",
    val district: String = "",
    val pinCode: String = "",
    val state: String = "",
    val isAvailableForRent: Boolean = true,
    val loading: Boolean = false,
    // Error Flags
    val addressLineError: Boolean = false,
    val cityError: Boolean = false,
    val districtError: Boolean = false,
    val pinCodeError: Boolean = false,
    val stateError: Boolean = false
)
