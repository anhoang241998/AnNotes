package com.annguyenhoang.annotes.presentation.notes

import com.annguyenhoang.annotes.data.remote.notes.NoteDto

sealed class NotesUiEvent {
    object FetchAllNotes : NotesUiEvent()
    data class ShowLoadingDialog(val isShow: Boolean) : NotesUiEvent()
    data class OnNotesChanged(val notes: List<NoteDto>) : NotesUiEvent()
}