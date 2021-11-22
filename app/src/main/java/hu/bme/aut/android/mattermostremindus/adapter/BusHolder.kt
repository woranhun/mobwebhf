package hu.bme.aut.android.mattermostremindus.adapter

object BusHolder {
    private var eventBus: EventBus? = null
    val instnace: EventBus?
        get() {
            if (eventBus == null) {
                eventBus = EventBus()
            }
            return eventBus
        }
}