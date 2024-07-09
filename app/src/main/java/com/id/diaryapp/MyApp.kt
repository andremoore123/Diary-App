package com.id.diaryapp

import android.app.Application
import com.id.diaryapp.di.AppModule
import com.id.diaryapp.di.DatabaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MyApp)
            modules(DatabaseModule.getModule())
            modules(AppModule.getModules())
        }
    }
}