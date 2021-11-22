package hu.bme.aut.android.mattermostremindus.services

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import hu.bme.aut.android.mattermostremindus.receivers.SendMessage
import hu.bme.aut.android.mattermostremindus.utils.Log.Companion.logTAG
import hu.bme.aut.android.mattermostremindus.utils.Message.Companion.messageidKEY


class MessageManagger : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        setAlarm(this, 10 * 1000L + System.currentTimeMillis(), 1.toString())
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        Toast.makeText(this, "MessageSender Stopped", Toast.LENGTH_LONG).show()
        cancelAlarm(this)
        Log.d(logTAG, "onDestroy")
    }


    companion object {
        const val TAG = "MessageSender"
    }

    private var pendingIntent: PendingIntent? = null

    private fun setAlarm(context: Context, alarmTime: Long, messageID: String) {
        val alarmManager: AlarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        Log.d(logTAG, "Alarm is created for id: $messageID !")
        val intent = Intent(context, SendMessage::class.java)
        intent.putExtra(messageidKEY, messageID)
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
}