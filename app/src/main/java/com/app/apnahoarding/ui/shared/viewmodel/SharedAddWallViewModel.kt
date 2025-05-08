package com.app.apnahoarding.ui.shared.viewmodel

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import com.app.apnahoarding.core.models.WallData
import com.app.apnahoarding.core.models.toMap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class SharedAddWallViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(WallData())
    val uiState: StateFlow<WallData> = _uiState

    private val _isUploading = MutableStateFlow(false)
    val isUploading: StateFlow<Boolean> = _isUploading
    private val _uploadProgress = MutableStateFlow(0f)
    val uploadProgress: StateFlow<Float> = _uploadProgress


    val userId = auth.currentUser?.uid.orEmpty()
    val postId = System.currentTimeMillis().toString()


    fun updateWallData(data: WallData) {
        _uiState.value = _uiState.value.copy(
            length = data.length,
            width = data.width,
            price = data.price,
        )
    }

    fun updateAddressFields(
        addressLine: String,
        city: String,
        district: String,
        pinCode: String,
        state: String,
        isAvailableForRent: Boolean,
        isApproved: Boolean = false
    ) {
        _uiState.value = _uiState.value.copy(
            addressLine = addressLine,
            city = city,
            district = district,
            pinCode = pinCode,
            state = state,
            isAvailableForRent = isAvailableForRent,
            isApproved = isApproved
        )
    }


    fun updateImageUri(index: Int, uri: Uri?) {
        val updatedUris = _uiState.value.imageUris.toMutableList()
        updatedUris[index] = uri.toString()
        _uiState.update { it.copy(imageUris = updatedUris) }
    }

    fun uploadImagesAndSave(
        localImageUris: List<String?>,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val storageRef = storage.reference
        val downloadUrls = mutableListOf<String>()
        var uploadedCount = 0

        // Null safety check
        val nonNullUris = localImageUris.filterNotNull()
        if (nonNullUris.isEmpty()) {
            onFailure(Exception("No valid images to upload"))
            return
        }

        // Optional: show progress UI
//        _uiState.update { it.copy(isUploading = true, uploadProgress = 0f) }
        _isUploading.value = true
        _uploadProgress.value = 0f

        nonNullUris.forEachIndexed { index, uriString ->
            val uri = uriString.toUri()
            val imageRef = storageRef.child("wall_images/$userId/$postId/image_$index.jpg")

            imageRef.putFile(uri)
                .continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let { throw it }
                    }
                    imageRef.downloadUrl
                }
                .addOnSuccessListener { url ->
                    downloadUrls.add(url.toString())
                    uploadedCount++

                    // Optional: update progress
//                    _uiState.update {
//                        it.copy(uploadProgress = uploadedCount.toFloat() / nonNullUris.size)
//                    }

                    _uploadProgress.value = uploadedCount.toFloat() / nonNullUris.size

                    if (uploadedCount == nonNullUris.size) {
                        _uiState.update {
                            it.copy(imageUris = downloadUrls)
                        }
                        _uploadProgress.value = 1f
                        _isUploading.value = false
                        saveWallData(_uiState.value, onSuccess, onFailure)
                    }
                }
                .addOnFailureListener { e ->
//                    _uiState.update { it.copy(isUploading = false) }
                    _isUploading.value = false
                    onFailure(e)
                }
        }
    }


    private fun saveWallData(
        wall: WallData,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val wallWithCurrentUserData = WallData.addCurrentUserData(auth, wall)
        firestore.collection("walls_db")                // Subcollection to hold user walls
            .document(postId.toString())              // Wall document
            .set(wallWithCurrentUserData.toMap())                                // Upload the wall data
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }

    }
}

