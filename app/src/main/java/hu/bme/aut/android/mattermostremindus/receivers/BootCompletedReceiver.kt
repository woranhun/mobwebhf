package hu.bme.aut.android.mattermostremindus.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import hu.bme.aut.android.mattermostremindus.services.MessageManagger
import hu.bme.aut.android.mattermostremindus.utils.Log.Companion.logTAG

class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            context.startService(Intent(context, MessageManagger::class.java))
            Log.i(logTAG, "BOOTCOMPLETED")
        }
    }
}