package com.id.diaryapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("Select * from noteentity")
    fun fetchNotes(): Flow<List<NoteEntity>>

    @Query("select * from noteentity where id = :id ")
    fun fetchNoteById(id: Int): NoteEntity?

    @Query("Delete From noteentity where id = :id")
    suspend fun deleteNoteById(id: Int)

    @Insert
    suspend fun insertNote(note: NoteEntity)

    @Query("SELECT * FROM noteentity WHERE date >= :startOfDay AND date < :endOfDay AND isNotify = 1")
    fun fetchNeedToNotifyNotes(startOfDay: Long, endOfDay: Long): Flow<List<NoteEntity>>

    @Update
    suspend fun updateNote(note: NoteEntity)
}