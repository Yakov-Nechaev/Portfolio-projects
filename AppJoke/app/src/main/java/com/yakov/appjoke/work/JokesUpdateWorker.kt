package com.yakov.appjoke.work

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.yakov.appjoke.R
import com.yakov.appjoke.app.App
import com.yakov.appjoke.app.SetupActivity
import com.yakov.appjoke.data.model.Joke
import com.yakov.appjoke.data.repository.DataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

private const val NOTIFICATION_ID = 1000

class JokesUpdateWorker(var context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private var repository = DataRepository(context.applicationContext as App, (applicationContext as App).jokeApi)
    private var alarmScheduler = AlarmScheduler(context)

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                val time = repository.getIntervalParam()
                val categories = repository.getCategoryParam()

                repository.getJokeByCategory(categories.name).let { joke ->
                    val notification = createNotification(joke)
                    notificationManager.notify(NOTIFICATION_ID, notification)
                    setUpNextUpdate(time)

                    Result.success()
                }
                Result.retry()
            } catch (e: Exception) {
                Result.failure()
            }
        }
    }

    private fun setUpNextUpdate(interval: Int) {
        alarmScheduler.schedule(interval)
    }
    private fun createNotification(joke: Joke): Notification {

        val id = applicationContext.getString(R.string.notification_channel_id)
        val title = applicationContext.getString(R.string.notification_title)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(id)
        }

        val mainIntent = Intent(applicationContext, SetupActivity::class.java)
        mainIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            mainIntent,
            PendingIntent.FLAG_MUTABLE
        )

        val showIntent = Intent(applicationContext, SaveJokeReceiver::class.java)
        val bundle = Bundle().apply {
            putString("id", joke.id)
            putString("url", joke.url)
            putString("value", joke.value)
            putString("icon_url", joke.iconUrl)
        }
        showIntent.putExtras(bundle)

        val showPendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            UUID.randomUUID().hashCode(),
            showIntent,
            PendingIntent.FLAG_MUTABLE
        )
        val showAction = NotificationCompat.Action.Builder(
            null,
            applicationContext.getString(R.string.add),
            showPendingIntent
        ).build()

        val cancelIntent = Intent(applicationContext, CancelUpdateReceiver::class.java)
        val cancelPendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            0,
            cancelIntent,
            PendingIntent.FLAG_MUTABLE
        )
        val cancelAction = NotificationCompat.Action.Builder(
            null,
            applicationContext.getString(R.string.cansel),
            cancelPendingIntent
        ).build()

        return NotificationCompat.Builder(applicationContext, id)
            .setContentTitle(title)
            .setTicker(title)
            .setContentText(joke.value)
            .setSmallIcon(R.drawable.baseline_circle_notifications_24)
            .setOngoing(false)
            .addAction(showAction)
            .addAction(cancelAction)
            .setContentIntent(pendingIntent)
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel(id: String) {
        val channel = NotificationChannel(
            id,
            applicationContext.getString(R.string.channel_title),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        private const val WORKER_TAG = "UPDATE JOKE WORKER"
        private const val WORK_NAME = "JOKE"

        fun enqueueWork(context: Context) {
            val workManager = WorkManager.getInstance(context)
            workManager.enqueueUniqueWork(
                WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                getOneTimeRequest()
            )
        }

        fun cancelWork(context: Context) {
            val workManager = WorkManager.getInstance(context)
            workManager.cancelAllWorkByTag(WORKER_TAG)
        }

        private fun getOneTimeRequest(): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<JokesUpdateWorker>()
                .addTag(WORKER_TAG)
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.UNMETERED)
                        .build()
                )
                .build()
        }
    }
}