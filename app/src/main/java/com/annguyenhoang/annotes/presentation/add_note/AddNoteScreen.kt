package com.annguyenhoang.annotes.presentation.add_note

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ContentAlpha
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.annguyenhoang.annotes.R
import com.annguyenhoang.annotes.data.remote.notes.NoteDto

@Composable
fun AddNoteScreen(
    modifier: Modifier = Modifier,
    noteTitle: String,
    noteContent: String,
    noteAuthor: String,
    isNoteSaved: Boolean,
    onBack: () -> Unit,
    onNoteTitleChanged: (String) -> Unit,
    onNoteContentChanged: (String) -> Unit,
    onSavedNote: (NoteDto) -> Unit
) {
    LaunchedEffect(isNoteSaved) {
        if (isNoteSaved)
            onBack()
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val newNote = NoteDto(
                        noteTitle = noteTitle,
                        noteContent = noteContent,
                        noteAuthor = noteAuthor
                    )
                    onSavedNote(newNote)
                },
                backgroundColor = Color(0xFF6200EE),
            ) {
                Icon(
                    Icons.Filled.Done,
                    stringResource(id = R.string.cd_save_note),
                    tint = Color.White
                )
            }
        }
    ) { paddingValues ->
        Column {
            AddNoteTopAppBar(R.string.add_note, onBack)
            AddNoteContent(
                noteTitle = noteTitle,
                noteContent = noteContent,
                onNoteTitleChanged = onNoteTitleChanged,
                onNoteContentChanged = onNoteContentChanged,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
fun AddNoteTopAppBar(@StringRes title: Int, onBack: () -> Unit) {
    TopAppBar(
        title = { Text(text = stringResource(title), color = Color.Black) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.Filled.ArrowBack,
                    stringResource(id = R.string.menu_back),
                    tint = Color.Black
                )
            }
        },
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = Color.White
    )
}

@Composable
fun AddNoteContent(
    noteTitle: String,
    noteContent: String,
    onNoteTitleChanged: (String) -> Unit,
    onNoteContentChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(all = 16.dp)
    ) {
        val textFieldColors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            cursorColor = MaterialTheme.colors.secondary.copy(alpha = ContentAlpha.high)
        )
        OutlinedTextField(
            value = noteTitle,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = onNoteTitleChanged,
            placeholder = {
                Text(
                    text = stringResource(id = R.string.title_hint),
                    style = MaterialTheme.typography.h6
                )
            },
            textStyle = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
            maxLines = 1,
            colors = textFieldColors
        )
        OutlinedTextField(
            value = noteContent,
            onValueChange = onNoteContentChanged,
            placeholder = { Text(stringResource(id = R.string.description_hint)) },
            modifier = Modifier
                .height(350.dp)
                .fillMaxWidth(),
            colors = textFieldColors
        )
    }
}