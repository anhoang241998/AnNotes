package com.annguyenhoang.annotes.login

import android.widget.Toast
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.annguyenhoang.annotes.R
import com.annguyenhoang.annotes.common.loading_dialog.LoadingDialog
import com.annguyenhoang.annotes.common.margin.MarginVertical
import com.annguyenhoang.annotes.data.remote.auth.UserDto

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    uiState: LoginUiState?,
    onLoginTapped: (String) -> Unit,
    onNavigateToNotesScreen: (UserDto?) -> Unit
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val username = remember { mutableStateOf(TextFieldValue("")) }
    val isDisableLoginButton = remember { mutableStateOf(username.value.text.isEmpty()) }
    val showDialog = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = username.value) {
        if (username.value.text.isEmpty()) return@LaunchedEffect
        isDisableLoginButton.value = false
    }

    LaunchedEffect(key1 = uiState) {
        uiState?.let { state ->
            when (state) {
                is LoginUiState.Data -> {
                    showDialog.value = false
                    state.data?.let(onNavigateToNotesScreen)
                }

                is LoginUiState.Error -> {
                    showDialog.value = false
                    Toast.makeText(context, "${state.error}", Toast.LENGTH_SHORT).show()
                }

                is LoginUiState.Loading -> {
                    showDialog.value = true
                }
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { focusManager.clearFocus() })
                }
        ) {
            LoginContents(username, isDisableLoginButton, onLoginTapped = { username ->
                onLoginTapped(username)
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
    Text(
        text = stringResource(id = R.string.app_name),
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.h3
    )
    MarginVertical(value = 8.dp)
    OutlinedTextField(
        username.value,
        label = { Text(text = stringResource(id = R.string.label_login)) },
        onValueChange = { newText: TextFieldValue ->
            username.value = newText
        })
    MarginVertical(value = 8.dp)
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
        Text(text = stringResource(id = R.string.login_btn).uppercase())
    }
}