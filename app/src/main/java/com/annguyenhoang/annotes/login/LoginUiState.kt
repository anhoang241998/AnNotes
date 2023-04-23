package com.annguyenhoang.annotes.login

import com.annguyenhoang.annotes.data.remote.auth.UserDto

/**
 * UiState for the login screen.
 */
data class LoginUiState(
    val isLoading: Boolean = false,
    val data: UserDto? = null,
    val error: String? = null
)