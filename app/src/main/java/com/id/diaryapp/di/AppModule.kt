package com.id.diaryapp.di

import com.id.diaryapp.MainActivity
import com.id.diaryapp.MainViewModel
import org.koin.dsl.module

object AppModule {
    private val viewModelModule = module {
        scope<MainActivity> { scoped { MainViewModel(get()) } }
    }

    fun getModules() = listOf(
        viewModelModule
    )
}