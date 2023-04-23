package com.annguyenhoang.annotes.data

import com.annguyenhoang.annotes.data.remote.auth.AuthNetworkSource
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