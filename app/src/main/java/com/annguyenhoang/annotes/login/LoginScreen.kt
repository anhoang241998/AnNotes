package com.annguyenhoang.annotes.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.annguyenhoang.annotes.R
import com.annguyenhoang.annotes.common.loading_dialog.LoadingDialog
import com.annguyenhoang.annotes.common.margin.MarginTop

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val username = remember { mutableStateOf(TextFieldValue("")) }
    val isDisableLoginButton = remember { mutableStateOf(username.value.text.isEmpty()) }
    val showDialog = remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val uiState = viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = username.value) {
        if (username.value.text.isEmpty()) return@LaunchedEffect
        isDisableLoginButton.value = false
    }

    LaunchedEffect(key1 = uiState.value) {
        showDialog.value = uiState.value.isLoading
        if (uiState.value.data != null) {
            Toast.makeText(context, "${uiState.value.data}", Toast.LENGTH_SHORT).show()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxSize()
                .focusRequester(focusRequester)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusRequester.requestFocus()
                    })
                }
        ) {
            LoginContents(username, isDisableLoginButton, onLoginTapped = { username ->
                viewModel.login(username)
            })
        }
        if (showDialog.value) {
            LoadingDialog()
        }
    }
}

@Composable
fun LoginContents(
    username: MutableState<TextFieldValue>,
    isDisableLoginButton: MutableState<Boolean>,
    onLoginTapped: (String) -> Unit
) {
    OutlinedTextField(
        username.value,
        label = { Text(text = stringResource(id = R.string.label_login)) },
        onValueChange = { newText: TextFieldValue ->
            username.value = newText
        })
    MarginTop(value = 8.dp)
    Button(
        colors = ButtonDefaults.buttonColors(
            disabledContentColor = Color.White,
            disabledBackgroundColor = Color.Gray
        ),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 4.dp,
            disabledElevation = 0.dp
        ),
        enabled = !isDisableLoginButton.value,
        onClick = { onLoginTapped(username.value.text) }) {
        Text(text = stringResource(id = R.string.login_btn))
    }
}