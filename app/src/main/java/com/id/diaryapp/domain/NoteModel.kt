package com.id.diaryapp.domain

data class NoteModel(
    val id: Int? = null,
    val title: String,
    val description: String,
    val isNotify: Boolean,
    val date: Long,
    val isDone: Boolean = false
)
