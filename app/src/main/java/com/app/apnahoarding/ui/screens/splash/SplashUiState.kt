package com.app.apnahoarding.ui.screens.splash

sealed class SplashUiState {
    object Loading : SplashUiState()              // Initial loading state
    object NavigateHome : SplashUiState()         // Navigate to Home screen
    object NavigateSignUp: SplashUiState()      // Navigate to Welcome/Register screen
    object InvalidUser : SplashUiState()          // User not logged in - navigate to login screen
    data class Error(val message: String) : SplashUiState() // Error state
}