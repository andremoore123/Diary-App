package com.id.diaryapp.worker

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.id.diaryapp.data.AppDatabase
import com.id.diaryapp.data.NoteEntity
import com.id.diaryapp.notification.HandleNotification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.Calendar
import java.util.concurrent.TimeUnit

class NotificationWorkManager(
    private val ctx: Context, params: WorkerParameters,
) : Worker(ctx, params), KoinComponent {
    private val appDatabase: AppDatabase by inject()
    private val notificationManager = NotificationManagerCompat.from(ctx)

    override fun doWork(): Result {
        val noteDao = appDatabase.noteDao()
        val currentDate = Calendar.getInstance()

        // Set the start of the day to 00:00:00
        currentDate.set(Calendar.HOUR_OF_DAY, 0)
        currentDate.set(Calendar.MINUTE, 0)
        currentDate.set(Calendar.SECOND, 0)
        currentDate.set(Calendar.MILLISECOND, 0)
        val startOfDay = currentDate.timeInMillis

        // Set the end of the day to 23:59:59
        val endOfDay = startOfDay + 24 * 60 * 60 * 1000 - 1
        val notes = runBlocking(Dispatchers.IO) {
            noteDao.fetchNeedToNotifyNotes(startOfDay, endOfDay).first()
        }
        notes.forEach {
            sendNotification(it)
        }
        return Result.success()
    }

    private fun sendNotification(note: NoteEntity) {
        val notification = HandleNotification(ctx).invoke(note.title, note.description)
        if (ActivityCompat.checkSelfPermission(
                ctx,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        notificationManager.notify(note.id!!, notification)
    }

    companion object {
        private const val TAG_OUTPUT = "Notification Work Manager"
        const val NOTIFICATION_WORK = "workNotification"

        fun buildDailyWorkRequest(): OneTimeWorkRequest {
            val currentDate = Calendar.getInstance()
            val dueDate = Calendar.getInstance()
            // Set Execution around 07:00:00 AM
            dueDate.set(Calendar.HOUR_OF_DAY, 7)
            dueDate.set(Calendar.MINUTE, 0)
            dueDate.set(Calendar.SECOND, 0)
            if (dueDate.before(currentDate)) {
                dueDate.add(Calendar.HOUR_OF_DAY, 24)
            }
            val timeDiff = dueDate.timeInMillis - currentDate.timeInMillis
            println("andre : time ${dueDate.timeInMillis}")
            return OneTimeWorkRequestBuilder<NotificationWorkManager>()
                .setInitialDelay(60000, TimeUnit.MILLISECONDS)
                .addTag(TAG_OUTPUT)
                .build()
        }
    }
}