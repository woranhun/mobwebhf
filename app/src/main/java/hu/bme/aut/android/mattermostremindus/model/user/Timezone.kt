package hu.bme.aut.android.mattermostremindus.model.user

data class Timezone(
    val automaticTimezone: String,
    val manualTimezone: String,
    val useAutomaticTimezone: Boolean
)