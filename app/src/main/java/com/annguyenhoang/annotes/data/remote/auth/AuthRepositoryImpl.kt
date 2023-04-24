package com.annguyenhoang.annotes.data.remote.auth

import com.annguyenhoang.annotes.data.AuthRepository
import com.annguyenhoang.annotes.presentation.login.LoginUiState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authNetworkSource: AuthNetworkSource
) : AuthRepository {

    override fun login(username: String): Flow<LoginUiState> {
        return authNetworkSource.login(username)
    }


}