package com.annguyenhoang.annotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.annguyenhoang.annotes.navigation.NoteNaveGraph
import com.annguyenhoang.annotes.utils.theme.NotesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteAppActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotesTheme {
                NoteNaveGraph()
            }
        }
    }
}