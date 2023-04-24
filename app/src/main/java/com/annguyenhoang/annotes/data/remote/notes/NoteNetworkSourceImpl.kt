package com.annguyenhoang.annotes.data.remote.notes

import com.annguyenhoang.annotes.presentation.notes.NotesUiState
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject

class NoteNetworkSourceImpl @Inject constructor(
    private val databaseRef: DatabaseReference
) : NoteNetworkSource {

    private val notes by lazy {
        mutableListOf<NoteDto>()
    }

    companion object {
        const val NOTES_NODE = "notes"
    }

    override fun fetchAllNotes() = callbackFlow {
        databaseRef.child(NOTES_NODE).get().addOnSuccessListener { result ->

            if (!result.exists()) {
                trySend(NotesUiState.Data(notes))
                return@addOnSuccessListener
            }

            try {
                val notesMap = result.children.associateBy(
                    { it.key!! },
                    { it.getValue(NoteDto::class.java)!! }
                )

                Timber.d("ANNHOLINH: $notesMap")

                notes.clear()
                notesMap.values.forEach { note ->
                    notes.add(note)
                }
                trySend(NotesUiState.Data(notes.reversed()))
            } catch (e: Exception) {
                trySend(NotesUiState.Error("Cannot fetch data: $e"))
            }
        }.addOnFailureListener {
            Timber.e(it)
            trySend(NotesUiState.Error("Error: $it"))
        }

        awaitClose {
            channel.close()
        }
    }

    override fun addNewNote(note: NoteDto) = createAndSendNewNoteToFirebase(note = note)

    private fun createAndSendNewNoteToFirebase(note: NoteDto) = callbackFlow {
        val noteId = UUID.randomUUID().toString()
        try {
            databaseRef.child(NOTES_NODE).get().addOnSuccessListener { result ->
                if (result.exists()) {
                    databaseRef.child(NOTES_NODE).updateChildren(mapOf(noteId to note))
                        .addOnCompleteListener {
                            trySend(true)
                        }
                } else {
                    databaseRef.child(NOTES_NODE).setValue(mapOf(noteId to note))
                        .addOnCompleteListener {
                            trySend(true)
                        }
                }
            }
        } catch (e: Exception) {
            Timber.e("$e")
            trySend(false)
        }

        awaitClose {
            channel.close()
        }
    }
}