package com.annguyenhoang.annotes.data.remote.notes

import com.annguyenhoang.annotes.presentation.notes.NotesUiState
import kotlinx.coroutines.flow.Flow

interface NoteNetworkSource {
    fun fetchAllNotes(): Flow<NotesUiState>
    fun addNewNote(note: NoteDto): Flow<Boolean>
}