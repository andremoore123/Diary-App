package com.id.diaryapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.id.diaryapp.domain.INoteRepository
import com.id.diaryapp.domain.NoteModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: INoteRepository
) : ViewModel() {
    val listNote = repository.fetchNotes()

    fun addNote(
        title: String,
        desc: String,
        date: Long,
        notify: Boolean
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val noteModel = NoteModel(
                title = title,
                description = desc,
                isNotify = notify,
                date = date,
            )
            repository.addNote(noteModel)
        }
    }
}