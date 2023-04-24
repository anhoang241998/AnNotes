package com.annguyenhoang.annotes.presentation.login

sealed class LoginUiEvent {
    data class EnteredUsernameTextField(val value: String) : LoginUiEvent()
    data class Login(val username: String) : LoginUiEvent()
    data class ShowLoadingDialog(val isShow: Boolean) : LoginUiEvent()
}