package hu.bme.aut.android.mattermostremindus.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import hu.bme.aut.android.mattermostremindus.adapter.BusHolder
import hu.bme.aut.android.mattermostremindus.adapter.MessageSentEvent
import hu.bme.aut.android.mattermostremindus.utils.Log.Companion.logTAG
import hu.bme.aut.android.mattermostremindus.utils.Message.Companion.todoidKEY


class SendMessage : BroadcastReceiver() {
    override fun onReceive(
        context: Context,
        intent: Intent
    ) {
        val todoid = intent.extras!!.getLong(todoidKEY)
        Log.d(logTAG, "Message just send withID: $todoid")
        //TODO call RESTAPI HERE AND CHECK STUFFS
        BusHolder.post(MessageSentEvent(todoid))
    }


}