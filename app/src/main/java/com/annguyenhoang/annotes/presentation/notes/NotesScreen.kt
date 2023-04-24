package com.annguyenhoang.annotes.presentation.notes

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.annguyenhoang.annotes.R
import com.annguyenhoang.annotes.common.loading_dialog.LoadingDialog
import com.annguyenhoang.annotes.common.margin.MarginTop
import com.annguyenhoang.annotes.data.remote.notes.NoteDto

@Composable
fun NotesScreen(
    modifier: Modifier = Modifier,
    notes: List<NoteDto>,
    isLoading: Boolean,
    notesUiState: NotesUiState?,
    onFetchAllNotes: () -> Unit,
    onNoteChanged: (List<NoteDto>) -> Unit,
    onShowLoading: (Boolean) -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        onFetchAllNotes()
    }

    LaunchedEffect(key1 = notesUiState) {
        notesUiState?.let { state ->
            when (state) {
                is NotesUiState.Data -> {
                    onShowLoading(false)
                    state.data?.let {
                        onNoteChanged(it)
                    }
                }
                is NotesUiState.Error -> {
                    onShowLoading(false)
                    Toast.makeText(context, "${state.error}", Toast.LENGTH_SHORT).show()
                }
                is NotesUiState.Loading -> {
                    onShowLoading(true)
                }
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                contentColor = Color.White,
                backgroundColor = Color(0xFF6200EE),
                modifier = Modifier.padding(end = 14.dp, bottom = 14.dp),
                onClick = { /* TODO */ }) {
                Icon(Icons.Filled.Add, contentDescription = stringResource(id = R.string.add_note))
            }
        }) { paddingValues ->
        LoadingContent(
            loading = isLoading,
            empty = notes.isEmpty(),
            emptyContent = { NotesEmptyContent() }) {
            NotesScreenContent(paddingValues = paddingValues, notes = notes)
        }
    }
}

@Composable
fun LoadingContent(
    loading: Boolean,
    empty: Boolean,
    emptyContent: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    if (empty && !loading) {
        emptyContent()
    } else if (!empty && !loading) {
        content()
    } else {
        LoadingDialog(loading)
    }
}


@Composable
fun NotesScreenContent(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    notes: List<NoteDto>
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.note_list),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        LazyColumn {
            items(notes) { note ->
                NoteItem(note = note)
            }
        }
    }
}

@Composable
private fun NoteItem(note: NoteDto) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = "Title: ${note.noteTitle}",
                style = MaterialTheme.typography.h5,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            MarginTop(value = 4.dp)
            Text(
                text = "Author: ${note.noteAuthor}",
                style = MaterialTheme.typography.h6
            )
            MarginTop(value = 8.dp)
            Text(
                text = note.noteContent,
                style = MaterialTheme.typography.body1
            )
        }
    }
}

@Composable
private fun NotesEmptyContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_no_fill),
            contentDescription = stringResource(R.string.no_note_all),
            modifier = Modifier.size(96.dp)
        )
        Text(stringResource(id = R.string.no_note_all))
    }
}