package hu.bme.aut.android.mattermostremindus.model.user

data class NotifyProps(
    val channel: Boolean,
    val desktop: String,
    val desktop_sound: Boolean,
    val email: Boolean,
    val first_name: Boolean,
    val mention_keys: String,
    val push: String
)