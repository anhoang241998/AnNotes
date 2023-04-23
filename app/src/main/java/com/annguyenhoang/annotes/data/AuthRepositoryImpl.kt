package com.annguyenhoang.annotes.data

import com.annguyenhoang.annotes.data.remote.auth.AuthNetworkSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authNetworkSource: AuthNetworkSource
) : AuthRepository {

    override fun login(username: String) {
        authNetworkSource.login(username)
    }

}