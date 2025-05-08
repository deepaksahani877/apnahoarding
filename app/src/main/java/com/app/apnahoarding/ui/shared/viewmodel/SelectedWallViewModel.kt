package com.app.apnahoarding.ui.shared.viewmodel

import androidx.lifecycle.ViewModel
import com.app.apnahoarding.core.models.WallData
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SelectedWallViewModel @Inject constructor(private val auth : FirebaseAuth) : ViewModel() {
    private val _selectedWall = MutableStateFlow<WallData?>(null)
    val selectedWall: StateFlow<WallData?> = _selectedWall

    fun setSelectedWall(wall: WallData) {
        _selectedWall.value = wall
    }
}
