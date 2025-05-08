package com.app.apnahoarding.core.models

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import java.time.LocalDate
import java.time.format.DateTimeFormatter


data class WallData(
    val length: String = "",
    val width: String = "",
    val price: String = "",
    val imageUris: List<String?> = listOf<String?>(null, null, null),
    val addressLine: String = "",
    val city: String = "",
    val district: String = "",
    val pinCode: String = "",
    val state: String = "",
    val isFeatured: Boolean = true,
    val isAvailableForRent: Boolean = true,
    val isApproved: Boolean = false,
    val uploaderUid: String = "",
    val uploadedBy: String = "",
    val createdAt: Timestamp = Timestamp.now() // <- Use Firestore Timestamp
) {

    companion object {
        fun addCurrentUserData(
            auth: FirebaseAuth,
            baseData: WallData
        ): WallData {
            val user = auth.currentUser
            return baseData.copy(
                uploaderUid = user?.uid.orEmpty(),
                uploadedBy = user?.displayName ?: user?.phoneNumber.orEmpty()
            )
        }
    }
}


private fun getCurrentDate(): String {
    val currentDate = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    return currentDate.format(formatter)
}


fun WallData.toMap(): Map<String, Any?> {
    return mapOf(
        "length" to length,
        "width" to width,
        "price" to price,
        "imageUris" to imageUris,
        "addressLine" to addressLine,
        "city" to city,
        "district" to district,
        "pinCode" to pinCode,
        "state" to state,
        "isFeatured" to isFeatured,
        "isAvailableForRent" to isAvailableForRent,
        "isApproved" to isApproved,
        "uploaderUid" to uploaderUid,
        "uploadedBy" to uploadedBy,
        "createdAt" to FieldValue.serverTimestamp()
    )
}
