package com.yakov.appjoke.work

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.yakov.appjoke.R

private const val NOTIFICATION_ID = 1000

class CancelUpdateReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            JokesUpdateWorker.cancelWork(context)
            AlarmScheduler(context).cancel()
            val notificationManager = it.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(NOTIFICATION_ID)
            val text = context.resources.getText(R.string.cansel)
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }
    }
}