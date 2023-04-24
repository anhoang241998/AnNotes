package com.annguyenhoang.annotes.data

import com.annguyenhoang.annotes.data.remote.notes.NoteDto
import com.annguyenhoang.annotes.presentation.notes.NotesUiState
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun fetchAllNotes(): Flow<NotesUiState>
    fun addNewNote(note: NoteDto): Flow<Boolean>
}