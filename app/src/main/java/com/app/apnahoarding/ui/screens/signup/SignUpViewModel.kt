package com.app.apnahoarding.ui.screens.signup

import androidx.lifecycle.ViewModel
import com.app.apnahoarding.core.repository.UserRepository
import com.app.apnahoarding.ui.screens.signup.data.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    fun registerUser(user: User, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        userRepository.registerUser(user, onSuccess, onError)
    }
}

