package hu.bme.aut.android.mattermostremindus.eventbus

import org.greenrobot.eventbus.EventBus


object BusHolder {
    private var eventBus: EventBus = EventBus()

    fun register(listener: BusHolderListener) {
        if (!eventBus.isRegistered(listener)) {
            eventBus.register(listener)
        }
    }

    fun unregister(listener: BusHolderListener) {
        eventBus.unregister(listener)
    }

    fun post(event: Any) {
        eventBus.post(event)
    }

}



