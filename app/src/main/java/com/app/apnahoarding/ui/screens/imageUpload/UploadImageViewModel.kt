package com.app.apnahoarding.ui.screens.imageUpload

import android.app.Application
import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.app.apnahoarding.core.repository.LocationRepository
import com.app.apnahoarding.utils.getAddressFromLocation
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class UploadImageViewModel @Inject constructor(
    application: Application,
    private val locationRepository: LocationRepository
) : AndroidViewModel(application) {

    private val _uiState = mutableStateOf(UploadImageUiState())
    val uiState: State<UploadImageUiState> = _uiState

    fun updateAvailability(value: Boolean) {
        _uiState.value = _uiState.value.copy(isAvailableForRent = value)
    }

    fun setLoading(value: Boolean) {
        _uiState.value = _uiState.value.copy(loading = value)
    }


    fun onAddressLineChanged(newValue: String) {
        _uiState.value = _uiState.value.copy(addressLine = newValue)
    }

    fun onCityChanged(newValue: String) {
        _uiState.value = _uiState.value.copy(city = newValue)
    }

    fun onDistrictChanged(newValue: String) {
        _uiState.value = _uiState.value.copy(district = newValue)
    }

    fun onPinCodeChanged(newValue: String) {
        _uiState.value = _uiState.value.copy(pinCode = newValue)
    }

    fun onStateChanged(newValue: String) {
        _uiState.value = _uiState.value.copy(state = newValue)
    }


    fun fetchAddressFromCoordinates(context: Context, lat: Double, lon: Double){
        val result = getAddressFromLocation(context, lat, lon)
        result?.let {
            _uiState.value = _uiState.value.copy(
                addressLine = it.addressLine,
                city = it.city,
                district = it.district,
                pinCode = it.pinCode,
                state = it.state
            )
        }
    }


    fun requestLocationIfPermitted(
        context: Context,
        onSuccess: (Pair<Double, Double>) -> Unit,
        onError: (Exception) -> Unit,
        onDenied: () -> Unit
    ) {
        if (locationRepository.areLocationPermissionsGranted(context)) {
            locationRepository.getCurrentLocation(context, onSuccess, onError)
        } else {
            onDenied()
        }
    }


    fun validateAddressForm(): Boolean {
        val current = _uiState.value

        val hasError = listOf(
            current.addressLine.isBlank(),
            current.city.isBlank(),
            current.district.isBlank(),
            current.pinCode.isBlank(),
            current.state.isBlank()
        ).any { it }

        _uiState.value = current.copy(
            addressLineError = current.addressLine.isBlank(),
            cityError = current.city.isBlank(),
            districtError = current.district.isBlank(),
            pinCodeError = current.pinCode.isBlank(),
            stateError = current.state.isBlank()
        )

        return !hasError
    }

}
