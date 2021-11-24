package hu.bme.aut.android.mattermostremindus.services

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import hu.bme.aut.android.mattermostremindus.data.TodoItem
import hu.bme.aut.android.mattermostremindus.data.TodoListDatabase
import hu.bme.aut.android.mattermostremindus.eventbus.BusHolder
import hu.bme.aut.android.mattermostremindus.eventbus.BusHolderListener
import hu.bme.aut.android.mattermostremindus.eventbus.MessageSentEvent
import hu.bme.aut.android.mattermostremindus.receivers.SendMessage
import hu.bme.aut.android.mattermostremindus.utils.Log.Companion.logTAG
import org.greenrobot.eventbus.Subscribe
import kotlin.concurrent.thread


class MessageManagger : Service(), BusHolderListener {
    private lateinit var database: TodoListDatabase
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        BusHolder.register(this)
        database = TodoListDatabase.getDatabase(this)
        calculateNextSend()
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        Toast.makeText(this, "MessageSender Stopped", Toast.LENGTH_LONG).show()
        cancelAlarm(this)
        BusHolder.unregister(this)
        Log.d(logTAG, "onDestroy")
    }


    companion object {
        const val TAG = "MessageSender"
    }

    private var pendingIntent: PendingIntent? = null
    private fun setAlarm(context: Context, alarmTime: Long) {
        val alarmManager: AlarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        Log.d(logTAG, "Alarm is created on [$alarmTime]!")
        val intent = Intent(context, SendMessage::class.java)
        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            alarmTime,
            pendingIntent
        )
    }

    private fun cancelAlarm(context: Context) {
        pendingIntent?.let {
            val alarmManager: AlarmManager =
                context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(it)
        }
    }

    @Subscribe
    override fun onMessageSent(event: MessageSentEvent) {
        thread {
            val item = database.todoItemDao().getFromID(event.todoId)
            if (item != null) {
                updateNextSendTime(item)
            }
            calculateNextSend()
        }
    }

    private fun updateNextSendTime(todoItem: TodoItem) {
        val now = System.currentTimeMillis()
        todoItem.previousSendInMs = now
        if (todoItem.isOn) {
            todoItem.nextSendInMs = now + todoItem.periodInMs
        }
        database.todoItemDao().update(todoItem)
        Log.d(logTAG, "Next Send Time updated for ${todoItem.id} to ${todoItem.nextSendInMs}")
    }

    private fun calculateNextSend() {
        thread {
            val nextSendItem = database.todoItemDao().getNextSend()
            cancelAlarm(this)
            if (nextSendItem != null) {
                nextSendItem.id?.let { setAlarm(this, nextSendItem.nextSendInMs) }
                Log.d(logTAG, "Next send is: ${nextSendItem.nextSendInMs}")
            }
        }
    }
}