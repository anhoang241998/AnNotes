package com.annguyenhoang.annotes.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.annguyenhoang.annotes.data.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val noteRepository: AuthRepository
) : ViewModel() {

    private val _uiState: MutableSharedFlow<LoginUiState?> = MutableSharedFlow()
    val uiState: SharedFlow<LoginUiState?> = _uiState

    fun login(username: String) = viewModelScope.launch {
        _uiState.emit(LoginUiState.Loading)
        noteRepository.login(username).collectLatest(_uiState::emit)
    }

}