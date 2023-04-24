package com.annguyenhoang.annotes.presentation.notes

import com.annguyenhoang.annotes.data.remote.notes.NoteDto

sealed class NotesUiState {
    object Loading : NotesUiState()
    data class Data(val data: List<NoteDto>?) : NotesUiState()
    data class Error(val error: String?) : NotesUiState()
}