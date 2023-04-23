package com.annguyenhoang.annotes.data

import com.annguyenhoang.annotes.login.LoginUiState
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun login(username: String): Flow<LoginUiState>
}