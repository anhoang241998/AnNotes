package com.annguyenhoang.annotes.data.remote.auth

import com.annguyenhoang.annotes.presentation.login.LoginUiState
import kotlinx.coroutines.flow.Flow

interface AuthNetworkSource {
    fun login(username: String): Flow<LoginUiState>
}