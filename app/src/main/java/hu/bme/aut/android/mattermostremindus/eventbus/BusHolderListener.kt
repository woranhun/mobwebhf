package hu.bme.aut.android.mattermostremindus.eventbus

interface BusHolderListener {
    fun onMessageSent(event: MessageSentEvent)
}