package com.yakov.appjoke.work

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.yakov.appjoke.app.App
import com.yakov.appjoke.data.model.Joke
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class SaveJokeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val bundle = intent.extras
        val joke = Joke(
            id = bundle?.getString("id") ?: "",
            url = bundle?.getString("url") ?: "",
            value = bundle?.getString("value") ?: "",
            iconUrl = bundle?.getString("icon_url") ?: ""
        )

        runAsync {
            (context.applicationContext as App).db.jokeDao().addJoke(joke)
        }.let {
            Toast.makeText(context, "joke added in favorites", Toast.LENGTH_SHORT).show()
        }
    }

    private fun runAsync(
        handler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, e ->
            Log.e("SaveJokeReceiver", "Coroutine error", e)
        },
        block: suspend () -> Unit
    ) {
        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO + handler).launch {
            try {
                block()
            } finally {
                if (handler.isActive) {
                    pendingResult.finish()
                }
            }
        }
    }
}