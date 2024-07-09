package com.id.diaryapp.domain

import kotlinx.coroutines.flow.Flow

interface INoteRepository {
    fun fetchNotes(): Flow<List<NoteModel>>
    fun fetchNoteById(id: Int): NoteModel?
    fun fetchNeedToNotifyNotes(): Flow<List<NoteModel>>

    suspend fun addNote(note: NoteModel)
    suspend fun removeNote(id: Int)
}