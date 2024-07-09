package com.id.diaryapp.data

import com.id.diaryapp.data.NoteMapper.noteEntityToModel
import com.id.diaryapp.data.NoteMapper.noteModelToEntity
import com.id.diaryapp.domain.INoteRepository
import com.id.diaryapp.domain.NoteModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NoteRepository(
    private val database: AppDatabase
): INoteRepository {
    private val noteDao = database.noteDao()
    override fun fetchNotes(): Flow<List<NoteModel>> = noteDao.fetchNotes().map { noteEntities ->
        noteEntities.map {
            noteEntityToModel(it)
        }
    }

    override fun fetchNoteById(id: Int): NoteModel? = noteDao.fetchNoteById(id)?.let {
        noteEntityToModel(it)
    }

    override fun fetchNeedToNotifyNotes(): Flow<List<NoteModel>> = noteDao.fetchNeedToNotifyNotes().map { noteEntities ->
        noteEntities.map {
            noteEntityToModel(it)
        }
    }

    override suspend fun addNote(note: NoteModel) = noteDao.insertNote(
        noteModelToEntity(note)
    )

    override suspend fun removeNote(id: Int) = noteDao.deleteNoteById(id)
    override suspend fun updateNote(note: NoteModel) {
        val noteEntity = noteModelToEntity(note)
        noteDao.updateNote(noteEntity)
    }
}