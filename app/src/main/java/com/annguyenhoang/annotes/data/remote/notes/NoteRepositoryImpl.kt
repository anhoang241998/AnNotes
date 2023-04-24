package com.annguyenhoang.annotes.data.remote.notes

import com.annguyenhoang.annotes.data.NoteRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepositoryImpl @Inject constructor(
    private val noteNetworkSource: NoteNetworkSource
) : NoteRepository {
    override fun fetchAllNotes() = noteNetworkSource.fetchAllNotes()
}