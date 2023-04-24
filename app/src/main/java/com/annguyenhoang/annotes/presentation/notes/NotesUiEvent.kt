package com.annguyenhoang.annotes.presentation.notes

import com.annguyenhoang.annotes.data.remote.notes.NoteDto

sealed class NotesUiEvent {
    object FetchAllNotes : NotesUiEvent()
    data class ShowLoadingDialog(val isShow: Boolean) : NotesUiEvent()
    data class OnNotesChanged(val notes: List<NoteDto>) : NotesUiEvent()
    data class OnNoteTitleChanged(val title: String) : NotesUiEvent()
    data class OnNoteContentChanged(val content: String) : NotesUiEvent()
    data class OnSavedNote(val note: NoteDto): NotesUiEvent()
}