package com.annguyenhoang.annotes.login

import com.annguyenhoang.annotes.data.remote.auth.UserDto

/**
 * UiState for the login screen.
 */
sealed class LoginUiState(
    data: UserDto?,
    error: String?
) {
    object Loading : LoginUiState(null, null)
    data class Data(val data: UserDto?) : LoginUiState(data, null)
    data class Error(val error: String?) : LoginUiState(null, error)
}