package com.annguyenhoang.annotes.presentation.login

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.annguyenhoang.annotes.data.AuthRepository
import com.annguyenhoang.annotes.utils.isNetworkAvailable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _usernameTextField = mutableStateOf("")
    val usernameTextField: State<String> = _usernameTextField

    private val _isShowDialog = mutableStateOf(false)
    val isShowDialog: State<Boolean> = _isShowDialog

    private val _uiState: Channel<LoginUiState?> = Channel()
    val uiState: Flow<LoginUiState?> = _uiState.receiveAsFlow()

    fun onEvent(event: LoginUiEvent) = viewModelScope.launch {
        when (event) {
            is LoginUiEvent.EnteredUsernameTextField -> {
                _usernameTextField.value = event.value
            }
            is LoginUiEvent.Login -> {
                _uiState.send(LoginUiState.Loading)
                if (isNetworkAvailable(getApplication<Application>().applicationContext)) {
                    authRepository.login(event.username).collectLatest(_uiState::send)
                } else {
                    _uiState.send(LoginUiState.Error("No Internet Connection"))
                }
            }
            is LoginUiEvent.ShowLoadingDialog -> {
                _isShowDialog.value = event.isShow
            }
        }
    }
}