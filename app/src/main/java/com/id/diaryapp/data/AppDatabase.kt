package com.id.diaryapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [NoteEntity::class], exportSchema = true, version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun noteDao(): NoteDao
}