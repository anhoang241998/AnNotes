package com.annguyenhoang.annotes.data.remote.notes

import com.annguyenhoang.annotes.presentation.notes.NotesUiState
import com.google.firebase.database.DatabaseReference
import com.google.gson.Gson
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber
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
            val valueNodes = result.value

            if (valueNodes == null) {
                trySend(NotesUiState.Data(notes))
                return@addOnSuccessListener
            }

            try {
                val notesJSON = valueNodes as String
                val firebaseNotes = Gson().fromJson(notesJSON, Array<NoteDto>::class.java).asList()
                notes.clear()
                notes.addAll(firebaseNotes)
                trySend(NotesUiState.Data(notes))
            } catch (e: Exception) {
                trySend(NotesUiState.Error("Cannot login: $e"))
            }
        }.addOnFailureListener {
            Timber.e(it)
            trySend(NotesUiState.Error("Error: $it"))
        }

        awaitClose {
            channel.close()
        }
    }

}