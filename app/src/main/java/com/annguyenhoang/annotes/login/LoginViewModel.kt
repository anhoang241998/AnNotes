package com.annguyenhoang.annotes.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.annguyenhoang.annotes.data.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val noteRepository: AuthRepository
) : ViewModel() {

    fun login(username: String) {
        viewModelScope.launch {
            noteRepository.login(username)
        }
    }

}