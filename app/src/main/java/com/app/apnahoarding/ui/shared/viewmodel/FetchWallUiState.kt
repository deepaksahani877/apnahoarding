package com.app.apnahoarding.ui.shared.viewmodel



sealed class FetchWallUiState<out T> {
    object Loading : FetchWallUiState<Nothing>()
    data class Success<T>(val data: T) : FetchWallUiState<T>()
    data class Error(val message: String?) : FetchWallUiState<Nothing>()
}
