package com.yakov.appjoke.work

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build

private const val ALARM_REQUEST_CODE = 1002

@SuppressLint("UnspecifiedImmutableFlag")
class AlarmScheduler(private val context: Context) {

    private val alarmManager: AlarmManager by lazy {
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    private val alarmIntent: PendingIntent
        get() =
            Intent(context, UpdateJokeReceiver::class.java).let { intent ->

                var pendingIntent: PendingIntent? = null
                pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    PendingIntent.getBroadcast(
                        context,
                        ALARM_REQUEST_CODE,
                        intent,
                        PendingIntent.FLAG_MUTABLE
                    )
                } else {
                    PendingIntent.getBroadcast(
                        context,
                        ALARM_REQUEST_CODE,
                        intent,
                        PendingIntent.FLAG_ONE_SHOT
                    )
                }
                pendingIntent
            }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun schedule(time: Int) {
        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + time * 1000,
            alarmIntent
        )
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun cancel() {
        alarmManager.cancel(alarmIntent)
    }
}