package com.app.apnahoarding.ui.shared.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.apnahoarding.core.models.WallData
import com.app.apnahoarding.core.repository.WallRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WallListViewModel @Inject constructor(
    private val wallRepository: WallRepository
) : ViewModel() {

    private val _featuredWallsState = MutableStateFlow<FetchWallUiState<List<WallData>>>(FetchWallUiState.Loading)
    val featuredWallsState: StateFlow<FetchWallUiState<List<WallData>>> = _featuredWallsState

    private val _recentWallState = MutableStateFlow<FetchWallUiState<List<WallData>>>(FetchWallUiState.Loading)
    val recentWallState: StateFlow<FetchWallUiState<List<WallData>>> = _recentWallState


    private val _allWallsState = MutableStateFlow<FetchWallUiState<List<WallData>>>(FetchWallUiState.Loading)
    val allWallsState: StateFlow<FetchWallUiState<List<WallData>>> = _allWallsState


    private val _paginatedWalls = MutableStateFlow<List<WallData>>(emptyList())
    val paginatedWalls: StateFlow<List<WallData>> = _paginatedWalls

    private var isLoadingMore = false
    private var lastUid: String? = null

    fun loadMoreWalls(limit: Long = 10) {
        if (isLoadingMore) return
        isLoadingMore = true

        viewModelScope.launch {
            try {
                val newWalls = wallRepository.getPaginatedWalls(lastUid, limit)
                if (newWalls.isNotEmpty()) {
                    lastUid = newWalls.last().uploaderUid
                    _paginatedWalls.value += newWalls
                }
            } catch (e: Exception) {
                // Handle error
            } finally {
                isLoadingMore = false
            }
        }
    }


    fun searchWalls(query: String) {
        val filtered = _paginatedWalls.value.filter {
            it.city.contains(query, true) ||
                    it.pinCode.contains(query, true) ||
                    "${it.length}x${it.width}".contains(query, true)
        }
        _paginatedWalls.value = filtered
    }

    fun resetAndLoad() {
        lastUid = null
        _paginatedWalls.value = emptyList()
        loadMoreWalls()
    }



    fun loadAllWalls(){
        viewModelScope.launch {
            _allWallsState.value = FetchWallUiState.Loading
            try{
                val walls = wallRepository.getAllWalls()
                _allWallsState.value = FetchWallUiState.Success(walls)
            }catch (e: Exception){
                _allWallsState.value = FetchWallUiState.Error(e.message)
            }
        }
    }

    fun loadFeaturedWalls() {
        viewModelScope.launch {
            _featuredWallsState.value = FetchWallUiState.Loading
            try {
                val walls = wallRepository.getFeaturedWalls()
                _featuredWallsState.value = FetchWallUiState.Success(walls)
            } catch (e: Exception) {
                _featuredWallsState.value = FetchWallUiState.Error(e.message)
            }
        }
    }

    fun loadRecentWalls(limit: Long) {
        viewModelScope.launch {
            _recentWallState.value = FetchWallUiState.Loading
            try {
                val walls = wallRepository.getRecentWalls(limit)
                _recentWallState.value = FetchWallUiState.Success(walls)
            } catch (e: Exception) {
                _recentWallState.value = FetchWallUiState.Error(e.message)
            }
        }
    }
}