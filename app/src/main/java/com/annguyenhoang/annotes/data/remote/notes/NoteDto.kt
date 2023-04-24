package com.annguyenhoang.annotes.data.remote.notes

import java.util.UUID

data class NoteDto(
    val noteId: String = UUID.randomUUID().toString(),
    val noteTitle: String,
    val noteContent: String,
    val noteAuthor: String
)