package com.annguyenhoang.annotes.data.remote.auth

interface AuthNetworkSource {
    fun login(username: String)
}