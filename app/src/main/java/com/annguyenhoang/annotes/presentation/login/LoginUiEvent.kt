package com.annguyenhoang.annotes.presentation.login

import android.content.Context

sealed class LoginUiEvent {
    data class EnteredUsernameTextField(val value: String) : LoginUiEvent()
    data class Login(val context: Context, val username: String) : LoginUiEvent()
    data class ShowLoadingDialog(val isShow: Boolean) : LoginUiEvent()
}