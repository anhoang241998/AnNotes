package com.annguyenhoang.annotes.presentation.notes

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.annguyenhoang.annotes.data.NoteRepository
import com.annguyenhoang.annotes.data.remote.notes.NoteDto
import com.annguyenhoang.annotes.utils.isNetworkAvailable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _noteList = mutableStateOf(mutableListOf<NoteDto>())
    val noteList: State<List<NoteDto>> = _noteList

    private val _isShowLoading = mutableStateOf(false)
    val isShowLoading: State<Boolean> = _isShowLoading

    private val _noteTitle = mutableStateOf("")
    val noteTitle: State<String> = _noteTitle

    private val _noteContent = mutableStateOf("")
    val noteContent: State<String> = _noteContent

    private val _isNoteSaved = mutableStateOf(false)
    val isNoteSaved: State<Boolean> = _isNoteSaved

    private val _uiState: Channel<NotesUiState?> = Channel()
    val uiState: Flow<NotesUiState?> = _uiState.receiveAsFlow()

    fun onEvent(event: NotesUiEvent) = viewModelScope.launch {
        when (event) {
            is NotesUiEvent.FetchAllNotes -> {
                _uiState.send(NotesUiState.Loading)
                if (isNetworkAvailable(getApplication<Application>().applicationContext)) {
                    noteRepository.fetchAllNotes().collect { state ->
                        _uiState.send(state)
                    }
                } else {
                    _uiState.send(NotesUiState.Error("No Internet Connection"))
                }
            }

            is NotesUiEvent.ShowLoadingDialog -> {
                _isShowLoading.value = event.isShow
            }

            is NotesUiEvent.OnNotesChanged -> {
                _noteList.value.clear()
                _noteList.value = event.notes.toMutableList()
            }

            is NotesUiEvent.OnNoteTitleChanged -> {
                _noteTitle.value = event.title
            }

            is NotesUiEvent.OnNoteContentChanged -> {
                _noteContent.value = event.content
            }

            is NotesUiEvent.OnSavedNote -> {
                noteRepository.addNewNote(event.note).collectLatest {
                    _isNoteSaved.value = it
                }
            }
        }
    }

}