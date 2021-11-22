package hu.bme.aut.android.mattermostremindus.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import hu.bme.aut.android.mattermostremindus.utils.Log.Companion.logTAG


class MessageSender : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        Toast.makeText(this, "MessageSender Stopped", Toast.LENGTH_LONG).show()
        Log.d(logTAG, "onDestroy")
    }

    override fun onStart(intent: Intent?, startid: Int) {
        Log.d(TAG, "MessageSender Started")
    }


    companion object {
        private const val TAG = "MessageSender"
    }
}