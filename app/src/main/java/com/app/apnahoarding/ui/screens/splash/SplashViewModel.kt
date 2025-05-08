package com.app.apnahoarding.ui.screens.splash


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.apnahoarding.core.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// ViewModel for SplashScreen to determine navigation based on user profile


@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    // Backing field to hold UI state
    private val _uiState = MutableStateFlow<SplashUiState>(SplashUiState.Loading)

    // Public immutable UI state
    val uiState: StateFlow<SplashUiState> = _uiState

    // Call this from Composable to check user profile and emit UI state
    fun checkUserProfile() {
        viewModelScope.launch {
            try {
                val isComplete = userRepository.isUserProfileComplete()
                val isUserLoggedIn = userRepository.isUserLoggedIn()
                _uiState.value = if (!isUserLoggedIn) {
                    SplashUiState.InvalidUser
                } else {
                    if (isComplete) {
                        SplashUiState.NavigateHome
                    } else {
                        SplashUiState.NavigateSignUp
                    }
                }
            } catch (e: Exception) {
                _uiState.value = SplashUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
