package hu.bme.aut.android.mattermostremindus.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import hu.bme.aut.android.mattermostremindus.services.MessageSender
import hu.bme.aut.android.mattermostremindus.utils.Log.Companion.logTAG

class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        //Toast.makeText(context, "BOOTCOMPLETED", Toast.LENGTH_LONG).show()
        context.startService(Intent(context, MessageSender::class.java))
        Log.i(logTAG, "BOOTCOMPLETED")
    }
}