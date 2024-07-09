package com.id.diaryapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val title: String,
    val description: String,
    val isNotify: Boolean,
    val date: Long,
    val isDone: Boolean = false
)
