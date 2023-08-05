package com.yakov.appjoke.work

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class UpdateJokeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            try {
                JokesUpdateWorker.enqueueWork(context)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}