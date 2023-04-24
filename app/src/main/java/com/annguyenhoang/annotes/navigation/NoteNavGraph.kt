package com.annguyenhoang.annotes.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.annguyenhoang.annotes.navigation.AppDestinationsArgs.USERNAME
import com.annguyenhoang.annotes.navigation.AppScreens.ADD_NOTE_SCREEN
import com.annguyenhoang.annotes.navigation.AppScreens.LOGIN_SCREEN
import com.annguyenhoang.annotes.navigation.AppScreens.NOTES_SCREEN
import com.annguyenhoang.annotes.presentation.add_note.AddNoteScreen
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
    const val ADD_NOTE_SCREEN = "ADD_NOTE_SCREEN"
}

/**
 * Arguments used in [AppDestinations] routes
 */
object AppDestinationsArgs {
    const val USERNAME = "userName"
}

/**
 * Destinations used in the [NoteAppActivity]
 */
object AppDestinations {
    const val LOGIN_ROUTE = LOGIN_SCREEN
    const val NOTES_ROUTE = "${NOTES_SCREEN}/{${USERNAME}}"
    const val ADD_NOTE_ROUTE = "${ADD_NOTE_SCREEN}/{${USERNAME}}"
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
                    navController.navigate("${NOTES_SCREEN}/${userDto!!.username}")
                })
        }
        composable(
            AppDestinations.NOTES_ROUTE, arguments = listOf(
                navArgument(USERNAME) {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { entry ->
            val noteAuthor = entry.arguments?.getString(USERNAME) ?: ""
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
                onShowLoading = { noteViewModel.onEvent(NotesUiEvent.ShowLoadingDialog(it)) },
                onAddBtnTapped = {
                    navController.navigate("${ADD_NOTE_SCREEN}/${noteAuthor}")
                }
            )
        }
        composable(
            AppDestinations.ADD_NOTE_ROUTE, arguments = listOf(
                navArgument(USERNAME) {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { entry ->
            val noteAuthor = entry.arguments?.getString(USERNAME) ?: ""
            val noteViewModel: NoteViewModel = hiltViewModel()
            val noteTitle = noteViewModel.noteTitle.value
            val noteContent = noteViewModel.noteContent.value
            val isNoteSaved = noteViewModel.isNoteSaved.value

            AddNoteScreen(
                noteTitle = noteTitle,
                noteContent = noteContent,
                noteAuthor = noteAuthor,
                isNoteSaved = isNoteSaved,
                onBack = { navController.popBackStack() },
                onNoteTitleChanged = { noteViewModel.onEvent(NotesUiEvent.OnNoteTitleChanged(it)) },
                onNoteContentChanged = { noteViewModel.onEvent(NotesUiEvent.OnNoteContentChanged(it)) },
                onSavedNote = { noteViewModel.onEvent(NotesUiEvent.OnSavedNote(it)) }
            )
        }
    }
}