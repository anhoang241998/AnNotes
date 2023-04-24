package com.annguyenhoang.annotes.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.annguyenhoang.annotes.navigation.AppScreens.LOGIN_SCREEN
import com.annguyenhoang.annotes.navigation.AppScreens.NOTES_SCREEN
import com.annguyenhoang.annotes.navigation.TodoDestinationsArgs.USER_ID
import com.annguyenhoang.annotes.presentation.login.LoginScreen
import com.annguyenhoang.annotes.presentation.login.LoginUiEvent
import com.annguyenhoang.annotes.presentation.login.LoginViewModel
import com.annguyenhoang.annotes.presentation.notes.NoteViewModel
import com.annguyenhoang.annotes.presentation.notes.NotesScreen
import com.annguyenhoang.annotes.presentation.notes.NotesUiEvent

/**
 * Screens used in [AppDestinations]
 */
private object AppScreens {
    const val LOGIN_SCREEN = "LOGIN_SCREEN"
    const val NOTES_SCREEN = "NOTES_SCREEN"
}

/**
 * Arguments used in [AppDestinations] routes
 */
object TodoDestinationsArgs {
    const val USER_ID = "userId"
}

/**
 * Destinations used in the [NoteAppActivity]
 */
object AppDestinations {
    const val LOGIN_ROUTE = LOGIN_SCREEN
    const val NOTES_ROUTE = "${NOTES_SCREEN}/{${USER_ID}}"
}

@Composable
fun NoteNaveGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = AppDestinations.LOGIN_ROUTE,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(AppDestinations.LOGIN_ROUTE) {
            val loginViewModel: LoginViewModel = hiltViewModel()
            val username = loginViewModel.usernameTextField.value
            val isShowDialog = loginViewModel.isShowDialog.value
            val loginUiState = loginViewModel.uiState.collectAsState(initial = null)

            LoginScreen(
                username = username,
                isShowDialog = isShowDialog,
                loginUiState = loginUiState.value,
                onChangedUsernameText = { text ->
                    loginViewModel.onEvent(LoginUiEvent.EnteredUsernameTextField(text))
                },
                onLoginTapped = { text ->
                    loginViewModel.onEvent(LoginUiEvent.Login(text))
                },
                onShowDialog = {
                    loginViewModel.onEvent(LoginUiEvent.ShowLoadingDialog(it))
                },
                onNavigateToNotesScreen = { userDto ->
                    navController.navigate("${NOTES_SCREEN}/${userDto!!.userId}")
                })
        }
        composable(AppDestinations.NOTES_ROUTE) {
            val noteViewModel: NoteViewModel = hiltViewModel()
            val notes = noteViewModel.noteList.value
            val isShowLoading = noteViewModel.isShowLoading.value
            val notesUiState = noteViewModel.uiState.collectAsState(initial = null)

            NotesScreen(
                notes = notes,
                isLoading = isShowLoading,
                notesUiState = notesUiState.value,
                onFetchAllNotes = { noteViewModel.onEvent(NotesUiEvent.FetchAllNotes) },
                onNoteChanged = { noteViewModel.onEvent(NotesUiEvent.OnNotesChanged(it)) },
                onShowLoading = { noteViewModel.onEvent(NotesUiEvent.ShowLoadingDialog(it)) }
            )
        }
    }
}