package com.id.diaryapp.di

import androidx.room.Room
import androidx.room.Room.databaseBuilder
import com.id.diaryapp.data.AppDatabase
import com.id.diaryapp.data.NoteRepository
import com.id.diaryapp.domain.INoteRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

object DatabaseModule {
    private val roomModule = module {
        single { databaseBuilder(androidContext(), AppDatabase::class.java, "diary_app_database").build()}
    }

    private val repositoryModule = module {
        single<INoteRepository> {NoteRepository(get())}
    }

    fun getModule() = listOf(
        roomModule,
        repositoryModule
    )
}