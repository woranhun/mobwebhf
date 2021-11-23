package hu.bme.aut.android.mattermostremindus.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import hu.bme.aut.android.mattermostremindus.adapter.BusHolder
import hu.bme.aut.android.mattermostremindus.adapter.MessageSentEvent
import hu.bme.aut.android.mattermostremindus.data.TodoListDatabase
import hu.bme.aut.android.mattermostremindus.utils.Log.Companion.logTAG
import kotlin.concurrent.thread


class SendMessage : BroadcastReceiver() {
    override fun onReceive(
        context: Context,
        intent: Intent
    ) {
        thread {
            val todos = TodoListDatabase.getDatabase(context).todoItemDao()
                .getMessagesToSend(System.currentTimeMillis())
            if (todos.isNotEmpty()) {
                for (todo in todos) {
                    Log.d(logTAG, "Message just send withID: ${todo.id}")
                    //TODO call RESTAPI HERE AND CHECK STUFFS
                    todo.id?.let { MessageSentEvent(it) }?.let { BusHolder.post(it) }
                }
            }
        }

    }


}