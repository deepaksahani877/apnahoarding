package com.app.apnahoarding.ui.screens.welcome

import androidx.lifecycle.ViewModel
import com.app.apnahoarding.core.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    suspend fun isUserProfileComplete(): Boolean {
        return userRepository.isUserProfileComplete()
    }
}
