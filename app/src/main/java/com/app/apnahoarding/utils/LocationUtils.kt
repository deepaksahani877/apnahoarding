package com.app.apnahoarding.utils

import android.content.Context
import android.location.Geocoder
import java.util.Locale

fun getAddressFromLocation(
    context: Context,
    lat: Double,
    lon: Double
): AddressComponents? {
    return try {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses = geocoder.getFromLocation(lat, lon, 1)

        if (addresses != null && addresses.isNotEmpty()) {
            val address = addresses[0]
            AddressComponents(
                addressLine = address.getAddressLine(0) ?: "",
                city = address.locality ?: "",
                district = address.subAdminArea ?: "",
                pinCode = address.postalCode ?: "",
                state = address.adminArea ?: ""
            )
        } else null
    } catch (e: Exception) {
        null
    }
}

data class AddressComponents(
    val addressLine: String,
    val city: String,
    val district: String,
    val pinCode: String,
    val state: String
)
