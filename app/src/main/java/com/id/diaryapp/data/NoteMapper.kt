package com.id.diaryapp.data

import com.id.diaryapp.domain.NoteModel

object NoteMapper {
    fun noteModelToEntity(q: NoteModel) = NoteEntity(
        title = q.title,
        description = q.description,
        isNotify = q.isNotify,
        date = q.date,
        isDone = q.isDone
    )

    fun noteEntityToModel(q: NoteEntity) = NoteModel(
        id = q.id ?: 0,
        title = q.title,
        description = q.description,
        isNotify = q.isNotify,
        date = q.date,
        isDone = q.isDone
    )
}