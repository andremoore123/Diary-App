package com.id.diaryapp

import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.id.diaryapp.domain.INoteRepository
import com.id.diaryapp.domain.NoteModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

sealed class Filter(@StringRes val title: Int, @StringRes val desc: Int) {
    data object NoFilter : Filter(R.string.title_no_filter, R.string.desc_no_filter)
    data object FinishedFilter :
        Filter(R.string.title_filter_finished, R.string.title_filter_finished)

    data object UnfinishedFilter :
        Filter(R.string.title_filter_un_finished, R.string.desc_filter_un_finished)
}

class MainViewModel(
    private val repository: INoteRepository
) : ViewModel() {
    /***
     * This List of notes is sorted by Done Status and Date by Default
     */
    val listNote = repository.fetchNotes().map { it ->
        it.sortedBy { it.isDone }.sortedBy { it.date }
    }
    private val _filter = MutableLiveData<Filter>(Filter.NoFilter)
    val filter = _filter

    fun setFilter(filter: Filter) {
        viewModelScope.launch(Dispatchers.Main) {
            _filter.postValue(filter)
        }
    }

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

    fun updateNote(
        note: NoteModel,
        title: String,
        desc: String,
        date: Long,
        notify: Boolean
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val newNoteModel = note.copy(
                title = title,
                description = desc,
                isNotify = notify,
                date = date,

            )
            repository.updateNote(newNoteModel)
        }
    }

    fun updateNoteStatus(
        noteModel: NoteModel
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val newNote = noteModel.copy(
                isDone = !noteModel.isDone
            )
            repository.updateNote(newNote)
        }
    }

    fun deleteNoteByID(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.removeNote(id)
        }
    }
}