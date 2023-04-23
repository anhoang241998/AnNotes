package com.annguyenhoang.annotes.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.annguyenhoang.annotes.data.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val noteRepository: AuthRepository
) : ViewModel() {

    private val _uiState: Channel<LoginUiState?> = Channel()
    val uiState: Flow<LoginUiState?> = _uiState.receiveAsFlow()

    fun login(username: String) = viewModelScope.launch {
        _uiState.send(LoginUiState.Loading)
        noteRepository.login(username).collectLatest(_uiState::send)
    }

}