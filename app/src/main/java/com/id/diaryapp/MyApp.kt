package com.id.diaryapp

import android.app.Application
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.id.diaryapp.di.AppModule
import com.id.diaryapp.di.DatabaseModule
import com.id.diaryapp.worker.NotificationWorkManager
import com.id.diaryapp.worker.NotificationWorkManager.Companion.NOTIFICATION_WORK
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
        runNotificationScheduler()
    }

    private fun runNotificationScheduler() {
        val instanceWorkManager = WorkManager.getInstance(this)
        val workerRequest = NotificationWorkManager.buildDailyWorkRequest()
        instanceWorkManager.beginUniqueWork(NOTIFICATION_WORK, ExistingWorkPolicy.REPLACE, workerRequest).enqueue()
    }
}