package hu.bme.aut.android.mattermostremindus.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import hu.bme.aut.android.mattermostremindus.utils.Log.Companion.logTAG
import hu.bme.aut.android.mattermostremindus.utils.Message.Companion.messageidKEY


class SendMessage() : BroadcastReceiver(){
    override fun onReceive(
        context: Context,
        intent: Intent
    ) {
        val messageid = intent.extras!!.get(messageidKEY)
        Log.d(logTAG, "Message just send withID: $messageid")
        //TODO call RESTAPI HERE AND CHECK STUFFS
    }


}