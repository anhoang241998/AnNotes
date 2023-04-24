package com.annguyenhoang.annotes.presentation.login

import android.widget.Toast
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.annguyenhoang.annotes.R
import com.annguyenhoang.annotes.common.loading_dialog.LoadingDialog
import com.annguyenhoang.annotes.common.margin.MarginVertical
import com.annguyenhoang.annotes.data.remote.auth.UserDto

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    username: String,
    isShowDialog: Boolean,
    loginUiState: LoginUiState?,
    onChangedUsernameText: (String) -> Unit,
    onLoginTapped: (username: String) -> Unit,
    onShowDialog: (Boolean) -> Unit,
    onNavigateToNotesScreen: (UserDto?) -> Unit
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = loginUiState) {
        loginUiState?.let { state ->
            when (state) {
                is LoginUiState.Data -> {
                    onShowDialog(false)
                    state.data?.let(onNavigateToNotesScreen)
                }

                is LoginUiState.Error -> {
                    onShowDialog(false)
                    Toast.makeText(context, "${state.error}", Toast.LENGTH_SHORT).show()
                }

                is LoginUiState.Loading -> {
                    onShowDialog(true)
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
            LoginContents(
                username = username,
                isDisabledLoginBtn = username.isEmpty(),
                focusManager = focusManager,
                onChangedUsernameText = onChangedUsernameText,
                onLoginTapped = { username ->
                    onLoginTapped(username)
                })
        }
        LoadingDialog(isShowDialog)
    }
}

@Composable
fun LoginContents(
    username: String,
    isDisabledLoginBtn: Boolean,
    focusManager: FocusManager,
    onChangedUsernameText: (String) -> Unit,
    onLoginTapped: (String) -> Unit
) {
    Text(
        text = stringResource(id = R.string.app_name),
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.h3
    )
    MarginVertical(value = 8.dp)
    OutlinedTextField(
        value = username,
        label = { Text(text = stringResource(id = R.string.label_login)) },
        onValueChange = onChangedUsernameText
    )
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
        enabled = !isDisabledLoginBtn,
        onClick = {
            focusManager.clearFocus()
            onLoginTapped(username)
        }) {
        Text(text = stringResource(id = R.string.login_btn).uppercase())
    }
}