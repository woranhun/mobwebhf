package hu.bme.aut.android.mattermostremindus.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import hu.bme.aut.android.mattermostremindus.data.TodoListDatabase
import hu.bme.aut.android.mattermostremindus.eventbus.BusHolder
import hu.bme.aut.android.mattermostremindus.eventbus.MessageSentEvent
import hu.bme.aut.android.mattermostremindus.network.NetworkManager
import hu.bme.aut.android.mattermostremindus.utils.Log.Companion.logTAG
import hu.bme.aut.android.mattermostremindus.utils.SharedPreferencies
import kotlin.concurrent.thread


class SendMessage : BroadcastReceiver() {
    private var db: TodoListDatabase? = null
    private fun onSuccess(id: Long) {
        thread {
            val todo =
                db?.todoItemDao()?.getFromID(id)
            if (todo != null) {
                todo.id?.let { MessageSentEvent(it) }?.let { BusHolder.post(it) }
            }
        }

    }

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {
        db = TodoListDatabase.getDatabase(context)
        thread {
            val todos = TodoListDatabase.getDatabase(context).todoItemDao()
                .getMessagesToSend(System.currentTimeMillis())
            if (todos.isNotEmpty()) {
                for (todo in todos) {
                    Log.d(logTAG, "Message just send withID: ${todo.id}")
                    todo.id?.let {
                        NetworkManager.sendMessageToUser(
                            SharedPreferencies.getToken(context).toString(),
                            SharedPreferencies.getUrl(context).toString(),
                            SharedPreferencies.getMyUser(context).toString(),
                            todo.sendTo,
                            todo.message,
                            it,
                            ::onSuccess
                        )
                    }
                }
            }
        }

    }


}