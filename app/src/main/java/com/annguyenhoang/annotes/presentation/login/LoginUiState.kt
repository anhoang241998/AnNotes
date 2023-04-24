package com.annguyenhoang.annotes.presentation.login

import com.annguyenhoang.annotes.data.remote.auth.UserDto

sealed class LoginUiState {
    object Loading : LoginUiState()
    data class Data(val data: UserDto?) : LoginUiState()
    data class Error(val error: String?) : LoginUiState()
}