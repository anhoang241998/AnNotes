package com.annguyenhoang.annotes.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.annguyenhoang.annotes.data.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val noteRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(username: String) = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        noteRepository.login(username).collectLatest { latestState ->
            _uiState.update {
                it.copy(
                    isLoading = latestState.isLoading,
                    data = latestState.data,
                    error = latestState.error
                )
            }
        }
    }

}