package com.app.apnahoarding.ui.screens.addWall


import android.net.Uri
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AddWallViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(AddWallUiState())
    val uiState: StateFlow<AddWallUiState> = _uiState

    fun onLengthChange(newLength: String) {
        _uiState.update { it.copy(length = newLength) }
    }

    fun onWidthChange(newWidth: String) {
        _uiState.update { it.copy(width = newWidth) }
    }

    fun onPriceChange(newPrice: String) {
        _uiState.update { it.copy(price = newPrice) }
    }

    fun updateImageUri( index: Int,uri: Uri?,) {
        val updatedUris = _uiState.value.imageUris.toMutableList()
        updatedUris[index] = uri
        _uiState.update { it.copy(imageUris = updatedUris) }
    }


    fun validateForm(): Boolean {
        var isValid = true

        // Validate length
        val lengthError = if (_uiState.value.length.isBlank() || _uiState.value.length.toIntOrNull() == null) {
            isValid = false
            "Please enter a valid length"
        } else null

        // Validate width
        val widthError = if (_uiState.value.width.isBlank() || _uiState.value.width.toIntOrNull() == null) {
            isValid = false
            "Please enter a valid width"
        } else null

        // Validate price
        val priceError = if (_uiState.value.price.isBlank() || _uiState.value.price.toDoubleOrNull() == null || _uiState.value.price.toDouble() <= 0) {
            isValid = false
            "Please enter a valid price"
        } else null

        // Validate images
        val imageError = _uiState.value.imageUris.any { it == null }

        isValid = !imageError;

        _uiState.update {
            it.copy(
                lengthError = lengthError,
                widthError = widthError,
                priceError = priceError,
                imageError = imageError
            )
        }

        return isValid
    }

}
