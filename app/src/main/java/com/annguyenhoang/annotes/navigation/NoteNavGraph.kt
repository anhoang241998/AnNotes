package com.annguyenhoang.annotes.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.annguyenhoang.annotes.login.LoginScreen
import com.annguyenhoang.annotes.navigation.AppScreens.LOGIN_SCREEN
import com.annguyenhoang.annotes.navigation.AppScreens.NOTES_SCREEN
import com.annguyenhoang.annotes.navigation.TodoDestinationsArgs.USER_ID
import com.annguyenhoang.annotes.notes.NotesScreen

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
    const val LOGIN_ROUTE = "$LOGIN_SCREEN"
    const val NOTES_ROUTE = "${NOTES_SCREEN}/${USER_ID}"
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
        composable(AppDestinations.LOGIN_ROUTE) { LoginScreen() }
        composable(AppDestinations.NOTES_ROUTE) { NotesScreen() }
    }
}